package com.play.quiz.social;

import java.time.LocalDate;

import com.play.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Which quiz is a day's, setting it on the day's first read. Its own bean and its own transaction:
 * the readers around it are read-only (a quiz handed out fills its questions' options in memory,
 * which a writable transaction would flush), and the one write of the day cannot happen in theirs.
 */
@Log4j2
@Component
@RequiredArgsConstructor
class DailyChallengeDays {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final QuizService quizService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long quizIdFor(final LocalDate day) {
        return dailyChallengeRepository.findById(day).map(DailyChallenge::getQuizId).orElseGet(() -> {
            // ponytail: two first reads at once each store a quiz and one goes unused; harmless,
            // and it can only happen at the turn of a day.
            Long quizId = quizService.storeGeneralKnowledgeQuiz(DailyChallengeService.QUESTIONS);
            dailyChallengeRepository.claimDay(day, quizId);
            log.info("Daily challenge for {} set", day);
            return dailyChallengeRepository.findById(day).map(DailyChallenge::getQuizId).orElse(quizId);
        });
    }
}
