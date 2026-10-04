package com.play.quiz.iq;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.domain.Account;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ExperiencePayout;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The IQ test: an adaptive run through the item bank, scored with {@link Rasch} and turned into
 * a number on the usual scale (mean 100, standard deviation 15).
 *
 * <p>Adaptive, because it is what makes a short test worth anything. Everyone starts in the
 * middle; each answer moves the ability estimate, and the next question is the one that would
 * tell us most about somebody standing there. Twenty-odd well-aimed items pin an ability down
 * about as tightly as sixty fixed ones, and nobody spends the test on questions far too easy or
 * far too hard for them.
 *
 * <p>Where the scale comes from is the part most tests on the internet quietly skip. Two answers
 * are given here, in order:
 * <ol>
 *   <li>Until {@link #MIN_NORM_SAMPLE} people have finished it, the estimate is read as standard
 *       deviations directly — which is only true if the takers are a cross-section of everybody,
 *       and that is stated in the result as {@code normed = false}.</li>
 *   <li>After that, the score is where this run falls among everyone else's <em>first</em> go,
 *       turned back into a score through the normal curve. That is a real norm, honestly of the
 *       app's own players rather than of the population.</li>
 * </ol>
 *
 * <p>Nothing about it is a clinical measurement: no supervision, no proctor, and a bank that is
 * the same for everybody. It is as accurate as a self-administered test can be — an ability
 * estimate with its error attached, and a scale that says what it is.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class IqService {

    /** Enough items for the estimate to settle, few enough that people finish. */
    static final int MIN_ITEMS = 18;
    static final int MAX_ITEMS = 30;
    /** Stop early once the ability is pinned this tightly: ±0.32 logits is under ±5 IQ points. */
    static final double TARGET_ERROR = 0.32;

    static final int SECONDS_PER_ITEM = 90;
    /** A few seconds' grace for a slow connection; past that the answer is late and counts wrong. */
    private static final int LATE_GRACE_SECONDS = 5;
    private static final Duration TEST_LIMIT = Duration.ofMinutes(40);
    /** A test left open longer than this is abandoned, not resumed. */
    private static final Duration RESUME_LIMIT = Duration.ofHours(2);

    /** Below this many finished first attempts, there is nothing worth norming against. */
    static final int MIN_NORM_SAMPLE = 100;

    /** Items are re-reckoned from real answers once this many people have seen them. */
    private static final int RECALIBRATE_AFTER = 60;

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final IqItemRepository itemRepository;
    private final IqSessionRepository sessionRepository;
    private final IqResponseRepository responseRepository;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    /** A test on the go, or a fresh one. Nothing is thrown away by asking twice. */
    @Transactional
    public IqState start() {
        Account player = currentAccount();

        Optional<IqSession> running = openSession(player.getAccountId());
        if (running.isPresent()) {
            // Resumed on the question it was left on, with its clock restarted: the alternative
            // is a player who reloads losing an item to a timer they never saw.
            log.info("Account {} resumed IQ test {} at {} answered", player.getAccountId(),
                    running.get().getSessionId(), running.get().getAnswered());
            return serve(running.get(), true);
        }

        IqSession session = new IqSession();
        session.setAccount(player);
        session.setStartedDate(LocalDateTime.now());
        session.setAttemptNo(sessionRepository
                .findByAccount_AccountIdAndFinishedDateIsNotNullOrderByFinishedDateDesc(player.getAccountId())
                .size());

        IqSession saved = sessionRepository.save(session);
        log.info("Account {} started IQ test {} (attempt {})", player.getAccountId(), saved.getSessionId(),
                saved.getAttemptNo());
        return serve(saved, true);
    }

    /**
     * Records an answer and hands back the next question, or the result if that was the last one.
     *
     * @param chosen which option was picked; -1 for a question given up on
     */
    @Transactional
    public IqState answer(final int chosen) {
        Account player = currentAccount();
        IqSession session = openSession(player.getAccountId())
                .orElseThrow(() -> new RecordNotFoundException("No test in progress"));
        // No question on screen: an answer sent twice, or one sent after the test was resumed
        // elsewhere. Nothing to score, so the session is handed back as it stands.
        if (Objects.isNull(session.getCurrentItemId())) {
            log.info("IQ test {}: answer with no question on screen, ignored", session.getSessionId());
            return serve(session, true);
        }
        IqItem item = itemRepository.findById(session.getCurrentItemId())
                .orElseThrow(() -> new RecordNotFoundException("No item in progress"));

        long seconds = Duration.between(session.getServedDate(), LocalDateTime.now()).toSeconds();
        boolean late = seconds > SECONDS_PER_ITEM + LATE_GRACE_SECONDS;
        boolean correct = !late && chosen == item.getAnswerIndex();

        responseRepository.save(IqResponse.builder()
                .session(session)
                .item(item)
                .chosenIndex(chosen)
                .correct(correct)
                .seconds((int) Math.min(seconds, SECONDS_PER_ITEM))
                .build());
        session.setAnswered(session.getAnswered() + 1);
        session.setCurrentItemId(null);
        log.debug("IQ test {}: item {} answered {} in {}s, correct {}, late {}", session.getSessionId(),
                item.getItemId(), chosen, seconds, correct, late);

        return serve(session, false);
    }

    /** What this player scored last, for the profile. */
    @Transactional(readOnly = true)
    public Optional<IqResult> latest() {
        return latest(currentAccount().getAccountId());
    }

    /** What any player scored last, for their profile as others see it. */
    @Transactional(readOnly = true)
    public Optional<IqResult> latest(final Long accountId) {
        return sessionRepository
                .findByAccount_AccountIdAndFinishedDateIsNotNullOrderByFinishedDateDesc(accountId)
                .stream().findFirst()
                .map(this::resultOf);
    }

    /**
     * Works out where the test stands and either sends the next question or ends it.
     *
     * @param resuming whether this is a question already on screen being sent again
     */
    private IqState serve(final IqSession session, final boolean resuming) {
        List<IqResponse> answers = responseRepository.findAnswers(session.getSessionId());
        Rasch.Ability ability = abilityFrom(answers);

        if (!resuming && isOver(session, ability, answers.size())) {
            return new IqState(null, true, finish(session, ability, answers));
        }

        IqItem next = resuming && Objects.nonNull(session.getCurrentItemId())
                ? itemRepository.findById(session.getCurrentItemId()).orElse(null)
                : null;
        if (Objects.isNull(next)) {
            next = chooseItem(ability.theta(), answers);
        }
        if (Objects.isNull(next)) {
            // The bank ran out, which only happens if it is smaller than MAX_ITEMS.
            log.warn("IQ test {}: item bank ran out after {} answers", session.getSessionId(), answers.size());
            return new IqState(null, true, finish(session, ability, answers));
        }

        session.setCurrentItemId(next.getItemId());
        session.setServedDate(LocalDateTime.now());
        sessionRepository.save(session);

        return new IqState(question(next, answers.size()), false, null);
    }

    /** The test still on the go; one left longer than RESUME_LIMIT is abandoned, for answers too. */
    private Optional<IqSession> openSession(final Long accountId) {
        return sessionRepository.findFirstByAccount_AccountIdAndFinishedDateIsNullOrderBySessionIdDesc(accountId)
                .filter(session -> session.getStartedDate().isAfter(LocalDateTime.now().minus(RESUME_LIMIT)));
    }

    private boolean isOver(final IqSession session, final Rasch.Ability ability, final int answered) {
        if (answered >= MAX_ITEMS) {
            return true;
        }
        if (Duration.between(session.getStartedDate(), LocalDateTime.now()).compareTo(TEST_LIMIT) > 0) {
            return true;
        }
        return answered >= MIN_ITEMS && ability.standardError() <= TARGET_ERROR;
    }

    private Rasch.Ability abilityFrom(final List<IqResponse> answers) {
        double[] difficulties = answers.stream().mapToDouble(answer -> answer.getItem().getDifficulty()).toArray();
        boolean[] correct = new boolean[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            correct[i] = answers.get(i).isCorrect();
        }
        return Rasch.estimate(difficulties, correct);
    }

    /**
     * The item that would tell us most about somebody of this ability, out of those not yet seen.
     *
     * <p>Picked at random from the best handful rather than the single best, so the same run of
     * answers does not always draw the same questions: an item bank this size would otherwise
     * leak through people comparing notes, and the first few items would be the same for everyone.
     */
    private IqItem chooseItem(final double theta, final List<IqResponse> answers) {
        Set<Long> seen = answers.stream()
                .map(answer -> answer.getItem().getItemId())
                .collect(Collectors.toSet());

        List<IqItem> best = itemRepository.findAll().stream()
                .filter(item -> !seen.contains(item.getItemId()))
                .sorted(Comparator.comparingDouble(item -> -Rasch.information(theta, item.getDifficulty())))
                .limit(5)
                .toList();

        return best.isEmpty() ? null : best.get(random.nextInt(best.size()));
    }

    private IqQuestion question(final IqItem item, final int answered) {
        return new IqQuestion(item.getItemType(), payloadOf(item), optionCount(item),
                answered + 1, MAX_ITEMS, SECONDS_PER_ITEM);
    }

    private JsonNode payloadOf(final IqItem item) {
        try {
            return objectMapper.readTree(item.getPayload());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Item " + item.getCode() + " has no readable payload", exception);
        }
    }

    /** How many options the browser will show: the options list, or the figures for odd-one-out. */
    private int optionCount(final IqItem item) {
        JsonNode payload = payloadOf(item);
        JsonNode options = payload.has("o") ? payload.get("o") : payload.get("g");
        return Objects.isNull(options) ? 0 : options.size();
    }

    /** Closes the test: the score, the norming, what the items learned, and the experience. */
    private IqResult finish(final IqSession session, final Rasch.Ability ability, final List<IqResponse> answers) {
        session.setFinishedDate(LocalDateTime.now());
        session.setCurrentItemId(null);
        session.setTheta(ability.theta());
        session.setStandardError(ability.standardError());
        score(session, ability);
        sessionRepository.save(session);

        learnFrom(answers, ability.theta());
        payFor(session);

        log.info("Account {} finished IQ test {}: theta {} (se {}) -> IQ {}",
                session.getAccount().getAccountId(), session.getSessionId(),
                ability.theta(), ability.standardError(), session.getIq());

        return resultOf(session);
    }

    /**
     * Turns an ability into a score. Against the app's own takers once there are enough of them;
     * against the model's standard normal until then.
     */
    private void score(final IqSession session, final Rasch.Ability ability) {
        long sample = sessionRepository.countFirstAttempts();
        if (sample >= MIN_NORM_SAMPLE) {
            long below = sessionRepository.countFirstAttemptsBelow(ability.theta());
            // Halfway between "better than everyone below" and "not better than anyone above",
            // so the best run ever recorded does not come out at the hundredth percentile.
            double share = (below + 0.5) / (sample + 1.0);
            session.setIq(iqOf(Rasch.normalQuantile(share)));
            session.setPercentile(percentileOf(share));
            session.setNormed(true);
            return;
        }

        // Read off the scale directly, so the prior's pull toward the middle has to come back
        // out first; ranking against other people (above) is not affected by it.
        double standing = Rasch.unshrunk(ability);
        session.setIq(iqOf(standing));
        session.setPercentile(percentileOf(Rasch.normalBelow(standing)));
        session.setNormed(false);
    }

    private static int iqOf(final double standardDeviations) {
        return (int) Math.round(100 + 15 * standardDeviations);
    }

    private static int percentileOf(final double share) {
        return (int) Math.max(1, Math.min(99, Math.round(share * 100)));
    }

    /**
     * What the run taught the item bank: how each item went, and how able the person answering it
     * turned out to be.
     *
     * <p>Once an item has been seen enough times its difficulty is re-reckoned from that — the
     * ability at which half the people who saw it got it right, which is what a Rasch difficulty
     * is. Seeded difficulties are a guess from the template's rules; this is the measurement.
     */
    private void learnFrom(final List<IqResponse> answers, final double theta) {
        log.debug("IQ items updated from {} answers at theta {}", answers.size(), theta);
        answers.forEach(answer -> {
            IqItem item = answer.getItem();
            item.setAttempts(item.getAttempts() + 1);
            item.setCorrectCount(item.getCorrectCount() + (answer.isCorrect() ? 1 : 0));
            item.setThetaSum(item.getThetaSum() + theta);
            recalibrate(item);
            itemRepository.save(item);
        });
    }

    private void recalibrate(final IqItem item) {
        if (item.getAttempts() < RECALIBRATE_AFTER) {
            return;
        }
        // Kept off 0 and 1: an item everybody gets right has no finite difficulty, and one bad
        // batch should not send it to either end.
        double share = Math.min(0.97, Math.max(0.03, (double) item.getCorrectCount() / item.getAttempts()));
        double meanAbility = item.getThetaSum() / item.getAttempts();
        double measured = meanAbility - Math.log(share / (1 - share));

        // Half of the way, so a difficulty walks to where the answers say it is rather than
        // jumping there on the strength of one recalculation.
        item.setDifficulty(Math.max(-4, Math.min(4, (item.getDifficulty() + measured) / 2)));
    }

    /** Finishing the test pays, once: it is a long sit, and only the first go is the real one. */
    private void payFor(final IqSession session) {
        if (session.getAttemptNo() > 0) {
            log.info("IQ test {} is attempt {}: no experience", session.getSessionId(), session.getAttemptNo());
            return;
        }
        userService.addExperience(session.getAccount().getAccountId(), ExperiencePayout.IQ_TEST);
        log.info("Account {} paid {} experience for IQ test {}", session.getAccount().getAccountId(),
                ExperiencePayout.IQ_TEST, session.getSessionId());
    }

    private IqResult resultOf(final IqSession session) {
        double standardError = Optional.ofNullable(session.getStandardError()).orElse(1.0);
        // The error is in ability units; a point of ability is fifteen points of score.
        int margin = (int) Math.round(1.96 * 15 * standardError);

        return new IqResult(session.getIq(), session.getIq() - margin, session.getIq() + margin,
                session.getPercentile(), Optional.ofNullable(session.getTheta()).orElse(0.0), standardError,
                session.isNormed(), session.getAnswered(), session.getAttemptNo(), session.getFinishedDate());
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }

    /** Where the test stands: the question to answer, or the result once there are no more. */
    public record IqState(IqQuestion question, boolean finished, IqResult result) {
    }
}
