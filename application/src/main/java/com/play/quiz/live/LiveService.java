package com.play.quiz.live;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.play.quiz.coin.Coins;
import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.domain.Account;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.translation.QuestionTranslationDto;
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
import com.play.quiz.util.ServerText;
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
 * <p>The same rooms also hold the pairs game (Mode.PAIRS, up to 4 players): a table of cards face
 * down; the player whose turn it is turns two over. A pair is theirs and they go again; anything
 * else is shown to everyone for a moment, turns back, and the next player goes. A turn not played
 * in time passes. The faces stay on the server until a card is turned, so a browser cannot peek.
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
    /** How long a wrong pair stays face up for everyone to see. */
    static final Duration MISMATCH = Duration.ofMillis(1500);
    /** Faces the browser can draw (FACES in components/social/pairs-board.jsx); a game uses some of them. */
    static final int FACES = 30;
    /** The fewest pairs a table may have; the most is one per face. */
    static final int MIN_PAIRS = 4;

    private static final String CODE_LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;

    /** What a player is sent: the room as they see it, an invitation, or the room going away. */
    public record LiveEvent(String type, RoomView room, String code, String mode, UserSummary from, String reason) {
        static LiveEvent room(final RoomView room) {
            return new LiveEvent("ROOM", room, room.code(), room.mode(), null, null);
        }
    }

    /** {@code coins}: what a game of pairs paid this player at the end. */
    public record PlayerView(UserSummary user, int score, int correct, boolean answered, Boolean lastCorrect,
                             int lastPoints, boolean left, boolean host, int coins) {}

    public record QuestionView(Long id, String content, List<QuestionTranslationDto> translations, List<AnswerDto> answers) {}

    /**
     * @param endsAt    when the current phase runs out (epoch ms), 0 when nothing is timed
     * @param serverNow the server's clock at sending, so a browser can correct its own
     * @param reveal    the right answer, from the reveal on
     * @param yourAnswer what this player picked for the current question, if anything
     */
    public record RoomView(String code, String mode, String phase, Long hostId, Long you, int index, int total,
                           int seconds, long endsAt, long serverNow, QuestionView question, MiniGameResult reveal,
                           AnswerDto yourAnswer, List<PlayerView> players, List<UserSummary> invited, PairsView pairs) {}

    /**
     * The pairs table: every card's face, null while face down on the table; who took each card;
     * which are turned this turn; and whose turn it is.
     */
    public record PairsView(List<Integer> cards, List<Long> takenBy, List<Integer> turned, Long turn) {}

    public record CreateInput(Mode mode, Set<Long> friendIds, Long groupId, Integer questions, Integer seconds) {}

    private final Map<String, LiveRoom> rooms = new ConcurrentHashMap<>();
    private final ScheduledExecutorService clock = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "live-matches");
        thread.setDaemon(true);
        return thread;
    });
    private final SecureRandom random = new SecureRandom();
    /**
     * Pairs wins paid today, per account. ponytail: in memory, so a restart forgets the day's count;
     * a column on Q_USER if the cap ever needs to hold across restarts.
     */
    private final Map<Long, Map.Entry<LocalDate, Integer>> paidWins = new ConcurrentHashMap<>();
    /** Pairs games played to the end today, per two players ("lowId-highId"), in memory like paidWins. */
    private final Map<String, Map.Entry<LocalDate, Integer>> gamesTogether = new ConcurrentHashMap<>();

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
        // A repeating task that throws is never run again, and silently: the guard keeps the sweep alive.
        clock.scheduleAtFixedRate(() -> {
            try {
                sweep();
            } catch (RuntimeException exception) {
                log.error("Live sweep failed; trying again in a minute", exception);
            }
        }, 1, 1, TimeUnit.MINUTES);
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
            throw new IllegalArgumentException(ServerText.t("err_invite_only_friends", "You can only invite your friends"));
        }
        if (mode == Mode.DUEL && friendIds.size() != 1) {
            throw new IllegalArgumentException(ServerText.t("err_duel_one_friend", "A duel is against one friend"));
        }

        Map<Long, Account> invite = new LinkedHashMap<>();
        friendIds.forEach(id -> invite.put(id, friends.get(id)));
        if (mode == Mode.ROOM && Objects.nonNull(input.groupId())) {
            if (!userGroupRepository.isMember(input.groupId(), me.getEmail())) {
                throw new IllegalArgumentException(ServerText.t("err_not_in_group", "You are not in that group"));
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

        // For pairs: how many pairs, and the seconds a turn has.
        int questions = mode == Mode.PAIRS ? clamp(input.questions(), MIN_PAIRS, FACES, 8)
                : clamp(input.questions(), 3, 20, mode == Mode.DUEL ? 7 : 10);
        int seconds = mode == Mode.PAIRS ? clamp(input.seconds(), 10, 30, 20) : clamp(input.seconds(), 5, 30, 15);
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
                throw new IllegalArgumentException(ServerText.t("err_join_room_first", "Join the room to see it"));
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
                    log.info("Account {} rejoined {} {}", me.getAccountId(), room.mode, room.code);
                }
                return view(room, me.getAccountId());
            }
            if (room.phase != Phase.LOBBY) {
                throw new IllegalArgumentException(ServerText.t("err_match_started_that", "That match has already started"));
            }
            if (room.mode == Mode.DUEL && !room.invited.containsKey(me.getAccountId())) {
                throw new IllegalArgumentException(ServerText.t("err_duel_other_players", "That duel is between two other players"));
            }
            if (room.players.size() >= room.maxPlayers) {
                throw new IllegalArgumentException(ServerText.t("err_room_full", "That room is full"));
            }
            room.invited.remove(me.getAccountId());
            room.declined.remove(me.getAccountId());
            room.players.put(me.getAccountId(), new Player(me.getAccountId(), me.getEmail(), UserSummary.of(me)));
            room.lastActivity = Instant.now();
            pushAll(room);
            log.info("Account {} joined {} {}: {} players", me.getAccountId(), room.mode, room.code, room.players.size());
            return view(room, me.getAccountId());
        }
    }

    /** An invitation turned down. A duel has nobody else to wait for, so it closes. */
    public void decline(final String code) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        synchronized (room) {
            if (room.invited.remove(me) == null) {
                log.info("Account {} declined {} {} but was not invited (or already answered)", me, room.mode, room.code);
                return;
            }
            room.declined.add(me);
            log.info("Account {} declined {} {}", me, room.mode, room.code);
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
            // From the results: nothing changes for the others, but a rematch will not invite them.
            if (player != null && room.phase == Phase.FINISHED) {
                player.left = true;
                log.info("Account {} left the results of {} {}", me.getAccountId(), room.mode, room.code);
                return;
            }
            if (player == null || room.over()) {
                log.info("Account {} left {} {} but was {}", me.getAccountId(), room.mode, room.code,
                        player == null ? "not a player" : "too late: " + room.phase);
                return;
            }
            presence.setActivity(me.getEmail(), null);
            log.info("Account {} left {} {} during {}", me.getAccountId(), room.mode, room.code, room.phase);
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
            } else if (room.mode == Mode.PAIRS) {
                // Leaving on your own turn passes it; during a wrong pair's show it passes anyway.
                if (room.phase == Phase.TURN && me.getAccountId().equals(room.turn)) {
                    nextTurn(room, room.index);
                } else {
                    pushAll(room);
                }
            } else if (allAnswered(room)) {
                reveal(room, room.index);
            } else {
                pushAll(room);
            }
        }
    }

    /**
     * "Play again" on a finished match: the first to ask turns the room back into a lobby, with
     * the same mode and settings, and invites the others who played to the end (the usual toast).
     * Everyone who asks after that just joins it. The host stays host unless they had left the
     * match, in which case whoever reopened it hosts.
     */
    public RoomView again(final String code) {
        Account me = currentAccount();
        LiveRoom room = room(code);
        synchronized (room) {
            if (room.phase == Phase.FINISHED) {
                if (!room.players.containsKey(me.getAccountId())) {
                    throw new IllegalArgumentException(ServerText.t("err_not_playing_match", "You are not playing in that match"));
                }
                reopen(room, me);
            }
            // Reopened already (or by this call): in like any invited player.
            return join(code);
        }
    }

    /** Called with the room's lock held. */
    private void reopen(final LiveRoom room, final Account me) {
        List<Player> before = List.copyOf(room.players.values());
        Player host = room.players.get(room.hostId);
        if (host == null || host.left) room.hostId = me.getAccountId();

        room.players.clear();
        room.invited.clear();
        room.declined.clear();
        room.players.put(me.getAccountId(), new Player(me.getAccountId(), me.getEmail(), UserSummary.of(me)));
        room.phase = Phase.LOBBY;
        room.index = -1;
        room.questions = List.of();
        room.answers.clear();
        room.reveal = null;
        room.phaseEndsAt = null;
        room.faces = new int[0];
        room.takenBy = new Long[0];
        room.turned.clear();
        room.turn = null;
        room.lastActivity = Instant.now();

        LiveEvent invitation = new LiveEvent("INVITE", null, room.code, room.mode.name(), UserSummary.of(me), null);
        before.stream()
                .filter(player -> !player.left && !player.id.equals(me.getAccountId()))
                .forEach(player -> {
                    room.invited.put(player.id, player.user);
                    send(player.email, invitation);
                });
        log.info("Account {} reopened {} {} for a rematch, inviting {}", me.getAccountId(), room.mode, room.code,
                room.invited.keySet());
    }

    public RoomView start(final String code) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        List<QuestionDto> questions;
        synchronized (room) {
            if (!me.equals(room.hostId)) {
                throw new IllegalArgumentException(ServerText.t("err_only_host_starts", "Only the host starts the match"));
            }
            if (room.phase != Phase.LOBBY) {
                throw new IllegalArgumentException(ServerText.t("err_match_started", "The match has already started"));
            }
            if (room.active().size() < 2) {
                throw new IllegalArgumentException(ServerText.t("err_wait_for_player", "Wait for someone to join first"));
            }
        }
        // Outside the lock: a database read, and nothing in the room depends on it yet. The pairs
        // game has no questions.
        questions = room.mode == Mode.PAIRS ? List.of() : questionService.getMiniGameQuestions(room.questionCount);
        if (questions.isEmpty() && room.mode != Mode.PAIRS) {
            throw new IllegalArgumentException(ServerText.t("err_no_questions_yet", "There are no questions to play yet"));
        }
        synchronized (room) {
            if (room.phase != Phase.LOBBY) {
                log.info("{} {} was already started or closed ({}) while its questions were read", room.mode, room.code, room.phase);
                return view(room, me);
            }
            room.questions = questions;
            if (room.mode == Mode.PAIRS) {
                room.faces = deal(room.questionCount, random);
                room.takenBy = new Long[room.faces.length];
            }
            room.invited.clear();
            room.phase = Phase.COUNTDOWN;
            room.phaseEndsAt = Instant.now().plus(COUNTDOWN);
            Presence.Activity activity = room.mode == Mode.DUEL ? Presence.Activity.DUEL : Presence.Activity.ROOM;
            room.active().forEach(player -> presence.setActivity(player.email, activity));
            schedule(room, COUNTDOWN, room.mode == Mode.PAIRS ? () -> nextTurn(room, -1) : () -> nextQuestion(room, -1));
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
                throw new IllegalArgumentException(ServerText.t("err_not_playing_match", "You are not playing in that match"));
            }
            if (room.phase != Phase.QUESTION || room.index != index || room.answers.containsKey(me)) {
                // Too late, or twice: the answer that counted stands.
                log.info("Account {} answer to question {} of {} ignored: phase {}, current question {}, already answered {}",
                        me, index, room.code, room.phase, room.index, room.answers.containsKey(me));
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
            log.debug("Account {} answered question {} of {}: correct {}, {} points", me, index, room.code,
                    correct, player.lastPoints);

            if (allAnswered(room)) {
                reveal(room, index);
            } else {
                pushAll(room);
            }
            return view(room, me);
        }
    }

    /** The pairs game: the player whose turn it is turns a card over. */
    public RoomView flip(final String code, final int card) {
        Long me = currentAccount().getAccountId();
        LiveRoom room = room(code);
        synchronized (room) {
            Player player = room.players.get(me);
            if (player == null || player.left) {
                throw new IllegalArgumentException(ServerText.t("err_not_playing_match", "You are not playing in that match"));
            }
            if (room.phase != Phase.TURN || !me.equals(room.turn) || card < 0 || card >= room.faces.length
                    || room.takenBy[card] != null || room.turned.contains(card)) {
                // Not their turn, a card that is not on the table, or a double click: nothing happens.
                log.debug("Account {} flip of card {} in {} ignored: phase {}, turn {}", me, card, room.code,
                        room.phase, room.turn);
                return view(room, me);
            }
            room.turned.add(card);
            if (room.turned.size() == 2) {
                int first = room.turned.get(0);
                if (room.faces[first] == room.faces[card]) {
                    room.takenBy[first] = me;
                    room.takenBy[card] = me;
                    player.score++;
                    log.debug("Account {} took a pair in {}: {} pairs", me, room.code, player.score);
                    if (Arrays.stream(room.takenBy).allMatch(Objects::nonNull)) {
                        finish(room);
                    } else {
                        // A pair earns another go.
                        beginTurn(room, me);
                    }
                    return view(room, me);
                }
                room.phase = Phase.MISMATCH;
                room.phaseEndsAt = Instant.now().plus(MISMATCH);
                int turn = room.index;
                schedule(room, MISMATCH, () -> nextTurn(room, turn));
            }
            pushAll(room);
            return view(room, me);
        }
    }

    /** Every pair's face, twice, shuffled: {@code pairs} different faces out of the FACES there are. */
    static int[] deal(final int pairs, final Random random) {
        List<Integer> faces = new ArrayList<>();
        for (int face = 0; face < FACES; face++) faces.add(face);
        Collections.shuffle(faces, random);
        List<Integer> deck = new ArrayList<>();
        faces.subList(0, pairs).forEach(face -> {
            deck.add(face);
            deck.add(face);
        });
        Collections.shuffle(deck, random);
        return deck.stream().mapToInt(Integer::intValue).toArray();
    }

    // ---- The match's own clock --------------------------------------------------------------

    /** The pairs game: the turn after turn {@code after} goes to the next player still in, in joining order. */
    private void nextTurn(final LiveRoom room, final int after) {
        synchronized (room) {
            if (room.over() || room.index != after) return;
            List<Player> order = new ArrayList<>(room.players.values());
            // The first turn goes to a random player.
            int from = room.turn == null ? random.nextInt(order.size()) - 1 : order.indexOf(room.players.get(room.turn));
            for (int step = 1; step <= order.size(); step++) {
                Player next = order.get(Math.floorMod(from + step, order.size()));
                if (!next.left) {
                    beginTurn(room, next.id);
                    return;
                }
            }
        }
    }

    /** Called with the room's lock held. */
    private void beginTurn(final LiveRoom room, final Long playerId) {
        room.index++;
        room.turn = playerId;
        room.turned.clear();
        room.phase = Phase.TURN;
        room.phaseEndsAt = Instant.now().plusSeconds(room.seconds);
        int turn = room.index;
        // Not played in time: the turn passes, and a card turned so far turns back.
        schedule(room, Duration.ofSeconds(room.seconds), () -> nextTurn(room, turn));
        pushAll(room);
    }

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
        if (room.mode == Mode.PAIRS && Arrays.stream(room.takenBy).allMatch(Objects::nonNull)) {
            payPairsWinners(room);
        }
        room.phase = Phase.FINISHED;
        room.phaseEndsAt = null;
        room.lastActivity = Instant.now();
        room.players.values().forEach(player -> presence.setActivity(player.email, null));
        pushAll(room);
        log.info("{} {} finished after {} of {} questions: {}", room.mode, room.code, room.index + 1,
                room.questions.size(), room.players.values().stream()
                        .map(player -> player.id + "=" + player.score + (player.left ? " (left)" : ""))
                        .toList());
    }

    /**
     * Called with the room's lock held. Only a game played to the last pair pays: one that ended
     * because the others left does not. Everyone on the top score wins, up to the day's cap each.
     */
    private void payPairsWinners(final LiveRoom room) {
        int best = room.players.values().stream().mapToInt(player -> player.score).max().orElse(0);
        LocalDate today = LocalDate.now();
        // Every two players at the table have played one more game together today.
        List<Long> ids = room.players.keySet().stream().sorted().toList();
        Map<Long, Integer> mostTogether = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                int games = gamesTogether.compute(ids.get(i) + "-" + ids.get(j), (key, before) ->
                        before == null || !today.equals(before.getKey()) ? Map.entry(today, 1)
                                : Map.entry(today, before.getValue() + 1)).getValue();
                mostTogether.merge(ids.get(i), games, Math::max);
                mostTogether.merge(ids.get(j), games, Math::max);
            }
        }
        room.players.values().stream()
                .filter(player -> !player.left && player.score == best)
                .forEach(player -> {
                    // Against the opponent they have played most today: a new face pays in full.
                    int coins = Coins.pairsWin(mostTogether.getOrDefault(player.id, 1));
                    if (coins == 0) {
                        log.info("Account {} won pairs {} but has played these players too often today to be paid",
                                player.id, room.code);
                        return;
                    }
                    Map.Entry<LocalDate, Integer> paid = paidWins.compute(player.id, (id, before) ->
                            before == null || !today.equals(before.getKey()) ? Map.entry(today, 1)
                                    : Map.entry(today, before.getValue() + 1));
                    if (paid.getValue() > Coins.PAIRS_PAID_WINS_PER_DAY) {
                        log.info("Account {} won pairs {} but had been paid for {} wins today", player.id, room.code,
                                Coins.PAIRS_PAID_WINS_PER_DAY);
                        return;
                    }
                    accountRepository.addCoins(player.id, coins);
                    player.coins = coins;
                    log.info("Account {} won pairs {} with {} pairs: +{} coins", player.id, room.code, best, coins);
                });
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
        log.info("{} {} closed: {}", room.mode, room.code, reason);
    }

    /** Once a minute: rooms nobody started, and finished ones whose results nobody needs now. */
    private void sweep() {
        Instant now = Instant.now();
        int[] expired = {0, 0};
        rooms.values().forEach(room -> {
            synchronized (room) {
                if (room.phase == Phase.LOBBY && room.lastActivity.plus(LOBBY_IDLE).isBefore(now)) {
                    close(room, "EXPIRED");
                    expired[0]++;
                } else if (room.phase == Phase.FINISHED && room.lastActivity.plus(KEEP_FINISHED).isBefore(now)) {
                    rooms.remove(room.code);
                    expired[1]++;
                }
            }
        });
        if (expired[0] + expired[1] > 0) {
            log.info("Live sweep: {} idle lobbies closed, {} finished rooms dropped, {} rooms left",
                    expired[0], expired[1], rooms.size());
        }
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
                        player.left, player.id.equals(room.hostId), player.coins))
                .toList();

        QuestionDto current = room.index >= 0 && room.index < room.questions.size() && !room.over()
                ? room.questions.get(room.index) : null;
        QuestionView question = current == null || room.phase == Phase.COUNTDOWN ? null
                : new QuestionView(current.getId(), current.getContent(), current.getTranslations(), current.getAnswers());

        return new RoomView(room.code, room.mode.name(), room.phase.name(), room.hostId, viewer,
                room.index, room.questions.isEmpty() ? room.questionCount : room.questions.size(), room.seconds,
                room.phaseEndsAt == null ? 0 : room.phaseEndsAt.toEpochMilli(), Instant.now().toEpochMilli(),
                question, showResult ? room.reveal : null, room.answers.get(viewer), players,
                List.copyOf(room.invited.values()), room.mode == Mode.PAIRS ? pairsView(room) : null);
    }

    private static PairsView pairsView(final LiveRoom room) {
        boolean ended = room.phase == Phase.FINISHED;
        List<Integer> cards = new ArrayList<>(room.faces.length);
        for (int card = 0; card < room.faces.length; card++) {
            boolean shown = ended || room.takenBy[card] != null || room.turned.contains(card);
            cards.add(shown ? room.faces[card] : null);
        }
        return new PairsView(cards, Arrays.asList(room.takenBy), List.copyOf(room.turned), room.turn);
    }

    // ---- Helpers ----------------------------------------------------------------------------

    LiveRoom room(final String code) {
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
