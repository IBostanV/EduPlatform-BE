package com.play.quiz.live;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.domain.Account;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.live.LiveRoom.Mode;
import com.play.quiz.live.LiveRoom.Phase;
import com.play.quiz.live.LiveRoom.Player;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserService;
import com.play.quiz.social.Presence;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Live matches: a duel between two friends, or a room of up to twenty that friends join by code.
 * Everyone gets the same question at the same moment; the server keeps the clock, marks the
 * answers (with the mini game's own check, so the right answer never reaches a browser before the
 * reveal) and pushes every change to each player over the app's socket.
 *
 * <p>Points: 500 for a right answer, and up to 500 more the faster it came.
 *
 * <p>ponytail: rooms live in this server's memory and its one timer thread; a restart ends the
 * matches in progress, and a second instance would not see the first one's rooms. Both are fine
 * for games that last a few minutes; a shared store and a broker relay are the upgrade.
 */
@Log4j2
@Service
public class LiveService {

    static final Duration COUNTDOWN = Duration.ofSeconds(3);
    static final Duration REVEAL = Duration.ofSeconds(4);
    /** A room nobody started is closed after this long. */
    static final Duration LOBBY_IDLE = Duration.ofMinutes(15);
    /** A finished room stays readable this long, for the results. */
    static final Duration KEEP_FINISHED = Duration.ofMinutes(10);
    static final int BASE_POINTS = 500;
    static final int SPEED_POINTS = 500;

    private static final String CODE_LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;

    /** What a player is sent: the room as they see it, an invitation, or the room going away. */
    public record LiveEvent(String type, RoomView room, String code, String mode, UserSummary from, String reason) {
        static LiveEvent room(final RoomView room) {
            return new LiveEvent("ROOM", room, room.code(), room.mode(), null, null);
        }
    }

    public record PlayerView(UserSummary user, int score, int correct, boolean answered, Boolean lastCorrect,
                             int lastPoints, boolean left, boolean host) {}

    public record QuestionView(Long id, String content, List<AnswerDto> answers) {}

    /**
     * @param endsAt    when the current phase runs out (epoch ms), 0 when nothing is timed
     * @param serverNow the server's clock at sending, so a browser can correct its own
     * @param reveal    the right answer, from the reveal on
     * @param yourAnswer what this player picked for the current question, if anything
     */
    public record RoomView(String code, String mode, String phase, Long hostId, Long you, int index, int total,
                           int seconds, long endsAt, long serverNow, QuestionView question, MiniGameResult reveal,
                           AnswerDto yourAnswer, List<PlayerView> players, List<UserSummary> invited) {}

    public record CreateInput(Mode mode, Set<Long> friendIds, Long groupId, Integer questions, Integer seconds) {}

    private final Map<String, LiveRoom> rooms = new ConcurrentHashMap<>();
    private final ScheduledExecutorService clock = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "live-matches");
        thread.setDaemon(true);
        return thread;
    });
    private final SecureRandom random = new SecureRandom();

    private final QuestionService questionService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final UserGroupRepository userGroupRepository;
    private final AuthenticationFacade authenticationFacade;
    private final SimpMessagingTemplate messaging;
    private final Presence presence;

    public LiveService(final QuestionService questionService, final UserService userService,
                       final AccountRepository accountRepository, final UserGroupRepository userGroupRepository,
                       final AuthenticationFacade authenticationFacade, final SimpMessagingTemplate messaging,
                       final Presence presence) {
        this.questionService = questionService;
        this.userService = userService;
        this.accountRepository = accountRepository;
        this.userGroupRepository = userGroupRepository;
        this.authenticationFacade = authenticationFacade;
        this.messaging = messaging;
        this.presence = presence;
        clock.scheduleAtFixedRate(this::sweep, 1, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    void stop() {
        clock.shutdownNow();
    }

    // ---- Actions ----------------------------------------------------------------------------

    /**
     * Opens a match with the signed-in player as its host. A duel names exactly one friend; a room
     * may invite friends and a chat group the host is in, and anyone given the code can join too.
     * A host has one match at a time: an earlier one they have not finished is closed.
     */
    public RoomView create(final CreateInput input) {
        Account me = currentAccount();
        Mode mode = Optional.ofNullable(input.mode()).orElse(Mode.ROOM);
        Set<Long> friendIds = Optional.ofNullable(input.friendIds()).orElse(Set.of());

        Map<Long, Account> friends = accountRepository.findFriends(me.getAccountId()).stream()
                .collect(Collectors.toMap(Account::getAccountId, account -> account, (first, second) -> first));
        if (!friends.keySet().containsAll(friendIds)) {
            throw new IllegalArgumentException("You can only invite your friends");
        }
        if (mode == Mode.DUEL && friendIds.size() != 1) {
            throw new IllegalArgumentException("A duel is against one friend");
        }

        Map<Long, Account> invite = new LinkedHashMap<>();
        friendIds.forEach(id -> invite.put(id, friends.get(id)));
        if (mode == Mode.ROOM && Objects.nonNull(input.groupId())) {
            if (!userGroupRepository.isMember(input.groupId(), me.getEmail())) {
                throw new IllegalArgumentException("You are not in that group");
            }
            accountRepository.findAllById(userGroupRepository.findUserIdsByUserGroupId(input.groupId())).stream()
                    .filter(account -> !account.getAccountId().equals(me.getAccountId()))
                    .forEach(account -> invite.putIfAbsent(account.getAccountId(), account));
        }

        rooms.values().stream()
                .filter(room -> me.getAccountId().equals(room.hostId) && !room.over())
                .toList()
                .forEach(room -> {
                    synchronized (room) {
                        close(room, "REPLACED");
                    }
                });

        int questions = clamp(input.questions(), 3, 20, mode == Mode.DUEL ? 7 : 10);
        int seconds = clamp(input.seconds(), 5, 30, 15);
        LiveRoom room = new LiveRoom(newCode(), mode, me.getAccountId(), questions, seconds);
        synchronized (room) {
            room.players.put(me.getAccountId(), new Player(me.getAccountId(), me.getEmail(), UserSummary.of(me)));
            invite.values().forEach(account -> room.invited.put(account.getAccountId(), UserSummary.of(account)));
            rooms.put(room.code, room);

            LiveEvent invitation = new LiveEvent("INVITE", null, room.code, mode.name(), UserSummary.of(me), null);
            invite.values().forEach(account -> send(account.getEmail(), invitation));
            log.info("Account {} opened {} {} inviting {}", me.getAccountId(), mode, room.code, invite.keySet());
            return view(room, me.getAccountId());
        }
    }

    public RoomView get(final String code) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        synchronized (room) {
            if (!room.players.containsKey(me) && !room.invited.containsKey(me)) {
                throw new IllegalArgumentException("Join the room to see it");
            }
            return view(room, me);
        }
    }

    public RoomView join(final String code) {
        Account me = currentAccount();
        LiveRoom room = room(code);
        synchronized (room) {
            if (room.players.containsKey(me.getAccountId())) {
                Player player = room.players.get(me.getAccountId());
                if (player.left && !room.over()) {
                    player.left = false;
                    pushAll(room);
                }
                return view(room, me.getAccountId());
            }
            if (room.phase != Phase.LOBBY) {
                throw new IllegalArgumentException("That match has already started");
            }
            if (room.mode == Mode.DUEL && !room.invited.containsKey(me.getAccountId())) {
                throw new IllegalArgumentException("That duel is between two other players");
            }
            if (room.players.size() >= room.maxPlayers) {
                throw new IllegalArgumentException("That room is full");
            }
            room.invited.remove(me.getAccountId());
            room.declined.remove(me.getAccountId());
            room.players.put(me.getAccountId(), new Player(me.getAccountId(), me.getEmail(), UserSummary.of(me)));
            room.lastActivity = Instant.now();
            pushAll(room);
            return view(room, me.getAccountId());
        }
    }

    /** An invitation turned down. A duel has nobody else to wait for, so it closes. */
    public void decline(final String code) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        synchronized (room) {
            if (room.invited.remove(me) == null) {
                return;
            }
            room.declined.add(me);
            if (room.mode == Mode.DUEL) {
                close(room, "DECLINED");
            } else {
                pushAll(room);
            }
        }
    }

    public void leave(final String code) {
        Account me = currentAccount();
        LiveRoom room = room(code);
        synchronized (room) {
            Player player = room.players.get(me.getAccountId());
            if (player == null || room.over()) {
                return;
            }
            presence.setActivity(me.getEmail(), null);
            if (room.phase == Phase.LOBBY) {
                if (me.getAccountId().equals(room.hostId)) {
                    close(room, "HOST_LEFT");
                    return;
                }
                room.players.remove(me.getAccountId());
                pushAll(room);
                return;
            }
            player.left = true;
            // Nobody left to play against: the match ends where it stands.
            if (room.active().size() < 2) {
                finish(room);
            } else if (allAnswered(room)) {
                reveal(room, room.index);
            } else {
                pushAll(room);
            }
        }
    }

    public RoomView start(final String code) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        List<QuestionDto> questions;
        synchronized (room) {
            if (!me.equals(room.hostId)) {
                throw new IllegalArgumentException("Only the host starts the match");
            }
            if (room.phase != Phase.LOBBY) {
                throw new IllegalArgumentException("The match has already started");
            }
            if (room.active().size() < 2) {
                throw new IllegalArgumentException("Wait for someone to join first");
            }
        }
        // Outside the lock: a database read, and nothing in the room depends on it yet.
        questions = questionService.getMiniGameQuestions(room.questionCount);
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("There are no questions to play yet");
        }
        synchronized (room) {
            if (room.phase != Phase.LOBBY) {
                return view(room, me);
            }
            room.questions = questions;
            room.invited.clear();
            room.phase = Phase.COUNTDOWN;
            room.phaseEndsAt = Instant.now().plus(COUNTDOWN);
            Presence.Activity activity = room.mode == Mode.DUEL ? Presence.Activity.DUEL : Presence.Activity.ROOM;
            room.active().forEach(player -> presence.setActivity(player.email, activity));
            schedule(room, COUNTDOWN, () -> nextQuestion(room, -1));
            pushAll(room);
            log.info("{} {} started with {} players", room.mode, room.code, room.players.size());
            return view(room, me);
        }
    }

    public RoomView answer(final String code, final int index, final AnswerDto choice) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        synchronized (room) {
            Player player = room.players.get(me);
            if (player == null || player.left) {
                throw new IllegalArgumentException("You are not playing in that match");
            }
            if (room.phase != Phase.QUESTION || room.index != index || room.answers.containsKey(me)) {
                // Too late, or twice: the answer that counted stands.
                return view(room, me);
            }
            QuestionDto question = room.questions.get(index);
            boolean correct = questionService.checkMiniGameAnswer(question.getId(), choice).correct();
            long total = Duration.ofSeconds(room.seconds).toMillis();
            long left = Math.max(0, Duration.between(Instant.now(), room.phaseEndsAt).toMillis());

            room.answers.put(me, choice);
            player.lastCorrect = correct;
            player.lastPoints = correct ? BASE_POINTS + Math.round(SPEED_POINTS * (float) left / total) : 0;
            player.score += player.lastPoints;
            if (correct) player.correct++;

            if (allAnswered(room)) {
                reveal(room, index);
            } else {
                pushAll(room);
            }
            return view(room, me);
        }
    }

    // ---- The match's own clock --------------------------------------------------------------

    private void nextQuestion(final LiveRoom room, final int after) {
        synchronized (room) {
            if (room.over() || room.index != after) return;
            if (room.index + 1 >= room.questions.size()) {
                finish(room);
                return;
            }
            room.index++;
            room.phase = Phase.QUESTION;
            room.answers.clear();
            room.reveal = null;
            room.players.values().forEach(player -> {
                player.lastCorrect = null;
                player.lastPoints = 0;
            });
            room.questionStartedAt = Instant.now();
            room.phaseEndsAt = room.questionStartedAt.plusSeconds(room.seconds);
            int index = room.index;
            schedule(room, Duration.ofSeconds(room.seconds), () -> {
                synchronized (room) {
                    reveal(room, index);
                }
            });
            pushAll(room);
        }
    }

    /** Called with the room's lock held. */
    private void reveal(final LiveRoom room, final int index) {
        if (room.phase != Phase.QUESTION || room.index != index) return;
        room.phase = Phase.REVEAL;
        // Asked with no pick at all, the check answers with what the right one was.
        room.reveal = questionService.checkMiniGameAnswer(room.questions.get(index).getId(), new AnswerDto());
        room.players.values().stream()
                .filter(player -> !room.answers.containsKey(player.id))
                .forEach(player -> {
                    player.lastCorrect = false;
                    player.lastPoints = 0;
                });
        room.phaseEndsAt = Instant.now().plus(REVEAL);
        schedule(room, REVEAL, () -> nextQuestion(room, index));
        pushAll(room);
    }

    /** Called with the room's lock held. */
    private void finish(final LiveRoom room) {
        cancelTimer(room);
        room.phase = Phase.FINISHED;
        room.phaseEndsAt = null;
        room.lastActivity = Instant.now();
        room.players.values().forEach(player -> presence.setActivity(player.email, null));
        pushAll(room);
        log.info("{} {} finished", room.mode, room.code);
    }

    /** Called with the room's lock held. */
    private void close(final LiveRoom room, final String reason) {
        cancelTimer(room);
        room.phase = Phase.CLOSED;
        rooms.remove(room.code);
        LiveEvent closed = new LiveEvent("CLOSED", null, room.code, room.mode.name(), null, reason);
        room.players.values().forEach(player -> {
            presence.setActivity(player.email, null);
            send(player.email, closed);
        });
        accountRepository.findAllById(room.invited.keySet()).forEach(account -> send(account.getEmail(), closed));
    }

    /** Once a minute: rooms nobody started, and finished ones whose results nobody needs now. */
    private void sweep() {
        Instant now = Instant.now();
        rooms.values().forEach(room -> {
            synchronized (room) {
                if (room.phase == Phase.LOBBY && room.lastActivity.plus(LOBBY_IDLE).isBefore(now)) {
                    close(room, "EXPIRED");
                } else if (room.phase == Phase.FINISHED && room.lastActivity.plus(KEEP_FINISHED).isBefore(now)) {
                    rooms.remove(room.code);
                }
            }
        });
    }

    private void schedule(final LiveRoom room, final Duration delay, final Runnable step) {
        cancelTimer(room);
        room.timer = clock.schedule(() -> {
            try {
                step.run();
            } catch (RuntimeException exception) {
                log.error("Live match {} could not move on", room.code, exception);
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
    }

    private static void cancelTimer(final LiveRoom room) {
        if (room.timer != null) {
            room.timer.cancel(false);
            room.timer = null;
        }
    }

    // ---- Views and pushes -------------------------------------------------------------------

    private static boolean allAnswered(final LiveRoom room) {
        return room.active().stream().allMatch(player -> room.answers.containsKey(player.id));
    }

    private void pushAll(final LiveRoom room) {
        room.players.values().stream()
                .filter(player -> !player.left || room.over())
                .forEach(player -> send(player.email, LiveEvent.room(view(room, player.id))));
    }

    private void send(final String email, final LiveEvent event) {
        messaging.convertAndSendToUser(email, RestEndpoint.WS_BROKER_LIVE, event);
    }

    private static RoomView view(final LiveRoom room, final Long viewer) {
        boolean showResult = room.phase == Phase.REVEAL || room.phase == Phase.FINISHED;
        boolean playing = room.phase != Phase.LOBBY;
        Comparator<Player> order = playing
                ? Comparator.comparingInt((Player player) -> player.score).reversed()
                : Comparator.comparing(player -> 0);

        List<PlayerView> players = room.players.values().stream()
                .sorted(order)
                .map(player -> new PlayerView(player.user, player.score, player.correct,
                        room.answers.containsKey(player.id),
                        showResult ? player.lastCorrect : null,
                        showResult ? player.lastPoints : 0,
                        player.left, player.id.equals(room.hostId)))
                .toList();

        QuestionDto current = room.index >= 0 && room.index < room.questions.size() && !room.over()
                ? room.questions.get(room.index) : null;
        QuestionView question = current == null || room.phase == Phase.COUNTDOWN ? null
                : new QuestionView(current.getId(), current.getContent(), current.getAnswers());

        return new RoomView(room.code, room.mode.name(), room.phase.name(), room.hostId, viewer,
                room.index, room.questions.isEmpty() ? room.questionCount : room.questions.size(), room.seconds,
                room.phaseEndsAt == null ? 0 : room.phaseEndsAt.toEpochMilli(), Instant.now().toEpochMilli(),
                question, showResult ? room.reveal : null, room.answers.get(viewer), players,
                List.copyOf(room.invited.values()));
    }

    // ---- Helpers ----------------------------------------------------------------------------

    private LiveRoom room(final String code) {
        return Optional.ofNullable(code).map(value -> rooms.get(value.trim().toUpperCase()))
                .orElseThrow(() -> new RecordNotFoundException("No match with code " + code));
    }

    private String newCode() {
        String code;
        do {
            StringBuilder builder = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                builder.append(CODE_LETTERS.charAt(random.nextInt(CODE_LETTERS.length())));
            }
            code = builder.toString();
        } while (rooms.containsKey(code));
        return code;
    }

    private static int clamp(final Integer value, final int min, final int max, final int fallback) {
        return value == null ? fallback : Math.max(min, Math.min(max, value));
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }
}
