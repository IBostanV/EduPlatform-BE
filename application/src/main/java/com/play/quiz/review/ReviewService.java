package com.play.quiz.review;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuizService;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The mistakes deck: every question a player gets wrong comes back for review on a ladder of
 * 1, 3 and 7 days. Answered right when it is due, it climbs a rung; right on the last rung, it is
 * learnt and leaves the deck. Wrong anywhere, at any time, puts it back at the bottom.
 *
 * <p>It is fed from one place: the marking of a finished run (UserQuizHistoryServiceImpl), so a
 * question met again in an ordinary quiz counts as much as one met in a review.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewService {

    /** Days until a question on each rung is due again. Past the last rung it is learnt. */
    static final int[] INTERVALS = {1, 3, 7};
    static final int PER_REVIEW = 10;

    private final MistakeRepository mistakeRepository;
    private final QuizService quizService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    /** {@code boxes}: how many questions sit on each rung. {@code nextDue} is null with nothing due later. */
    public record Status(int due, int total, LocalDate nextDue, List<Long> boxes) {}

    /** Where a question goes after an answer: its rung and due date, or empty once it is learnt. */
    record Step(int box, LocalDate due) {}

    static Optional<Step> next(final Integer box, final LocalDate due, final boolean right, final LocalDate today) {
        if (!right) return Optional.of(new Step(0, today.plusDays(INTERVALS[0])));
        // Right on a question not in the deck: nothing to file. Right before it is due: it stays put.
        if (Objects.isNull(box)) return Optional.empty();
        if (due.isAfter(today)) return Optional.of(new Step(box, due));
        int climbed = box + 1;
        return climbed >= INTERVALS.length ? Optional.empty() : Optional.of(new Step(climbed, today.plusDays(INTERVALS[climbed])));
    }

    /** A marked run's answers, into the deck. Answers with no question (custom quizzes) are left out. */
    @Transactional
    public void record(final Long accountId, final Collection<HistoryAnswer> answers) {
        Map<Long, Boolean> rightByQuestion = answers.stream()
                .filter(answer -> Objects.nonNull(answer.getQuestionId()))
                // A question answered in several parts is right only if every part was.
                .collect(Collectors.toMap(HistoryAnswer::getQuestionId, answer -> Objects.isNull(answer.getRightAnswer()),
                        Boolean::logicalAnd));
        if (rightByQuestion.isEmpty()) return;

        LocalDate today = LocalDate.now();
        Map<Long, Mistake> deck = mistakeRepository.findByAccountIdAndQuestionIdIn(accountId, rightByQuestion.keySet())
                .stream().collect(Collectors.toMap(Mistake::getQuestionId, Function.identity()));

        rightByQuestion.forEach((questionId, right) -> {
            Mistake card = deck.get(questionId);
            Optional<Step> step = next(Objects.isNull(card) ? null : card.getBox(),
                    Objects.isNull(card) ? today : card.getDueDate(), right, today);
            if (step.isEmpty()) {
                if (Objects.nonNull(card)) {
                    mistakeRepository.delete(card);
                    log.debug("Account {} learnt question {}", accountId, questionId);
                }
                return;
            }
            Mistake saved = Objects.isNull(card)
                    ? Mistake.builder().accountId(accountId).questionId(questionId).build()
                    : card;
            saved.setBox(step.get().box());
            saved.setDueDate(step.get().due());
            mistakeRepository.save(saved);
        });
    }

    @Transactional(readOnly = true)
    public Status status() {
        Long me = currentAccountId();
        LocalDate today = LocalDate.now();
        List<Mistake> deck = mistakeRepository.findByAccountId(me);
        Map<Integer, Long> byBox = deck.stream().collect(Collectors.groupingBy(Mistake::getBox, Collectors.counting()));
        return new Status(
                (int) deck.stream().filter(card -> !card.getDueDate().isAfter(today)).count(),
                deck.size(),
                deck.stream().map(Mistake::getDueDate).filter(day -> day.isAfter(today)).min(LocalDate::compareTo).orElse(null),
                java.util.stream.IntStream.range(0, INTERVALS.length).mapToObj(box -> byBox.getOrDefault(box, 0L)).toList());
    }

    /**
     * Today's due questions, at most {@value #PER_REVIEW}, as a stored quiz to play. Not one
     * transaction: storing the quiz writes, and handing it out must be read-only.
     */
    public QuizDto quiz() {
        Long me = currentAccountId();
        Set<Long> due = mistakeRepository.findByAccountIdAndDueDateLessThanEqualOrderByDueDate(me, LocalDate.now(),
                        PageRequest.of(0, PER_REVIEW)).stream()
                .map(Mistake::getQuestionId)
                .collect(Collectors.toSet());
        if (due.isEmpty()) {
            throw new IllegalArgumentException(ServerText.t("err_review_nothing_due", "Nothing to review today"));
        }
        return quizService.replay(quizService.storeQuestionsQuiz(due));
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
