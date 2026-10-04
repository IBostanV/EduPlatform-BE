package com.play.quiz.duel;

import java.time.LocalDateTime;

import com.play.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Which quiz a duel round is, storing it when the round is first played. Its own bean and its own
 * transaction, as for the daily challenge: the reader around it is read-only (a quiz handed out
 * fills its questions' options in memory, which a writable transaction would flush).
 */
@Log4j2
@Component
@RequiredArgsConstructor
class DuelRounds {

    static final int QUESTIONS = 5;

    private final DuelRoundRepository roundRepository;
    private final QuizService quizService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long quizIdFor(final Long duelId, final int roundNo) {
        DuelRound.Key key = new DuelRound.Key(duelId, roundNo);
        return roundRepository.findById(key).map(DuelRound::getQuizId).orElseGet(() -> {
            // Only the player whose turn it is gets here, so two first reads at once are one
            // player in two tabs; the round's key keeps it to one quiz either way.
            Long quizId = quizService.storeGeneralKnowledgeQuiz(QUESTIONS);
            roundRepository.save(DuelRound.builder()
                    .duelId(duelId)
                    .roundNo(roundNo)
                    .quizId(quizId)
                    .createdDate(LocalDateTime.now())
                    .build());
            log.info("Duel {} round {} set to quiz {}", duelId, roundNo, quizId);
            return quizId;
        });
    }
}
