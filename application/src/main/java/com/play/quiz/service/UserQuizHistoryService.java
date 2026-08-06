package com.play.quiz.service;

import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.record.PageResponse;
import com.play.quiz.record.QuizHistoryEntry;
import com.play.quiz.record.QuizStatistics;
import org.springframework.stereotype.Service;

@Service
public interface UserQuizHistoryService {

    UserQuizHistoryDto save(final UserQuizHistoryDto userQuizHistoryDto);

    UserQuizHistoryDto getById(final Long historyId);

    /** One page of the signed-in player's own finished runs, newest first. */
    PageResponse<QuizHistoryEntry> getOwnHistory(int page, int size);

    /** What all of their runs add up to — the whole history, not the page above. */
    QuizStatistics getOwnStatistics();
}
