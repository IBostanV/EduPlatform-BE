package com.play.quiz.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.service.CustomQuizService;
import com.play.quiz.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** The Take quiz filters travel as query parameters; this is them landing in UserQuizParams. */
@ExtendWith(MockitoExtension.class)
class QuizControllerParamsTest {

    @Mock private QuizService quizService;
    @Mock private CustomQuizService customQuizService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new QuizController(quizService, customQuizService)).build();
    }

    @Test
    void given_filters_in_the_query_when_getting_a_categorized_quiz_then_bind_them_all() throws Exception {
        when(quizService.getQuizByCategoryAndParams(eq(7L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(QuizDto.builder().build());

        mockMvc.perform(get("/api/quiz/categorized/{catId}", 7)
                        .param("quizType", "2")
                        .param("questionCount", "5")
                        .param("complexityFrom", "1")
                        .param("complexityTo", "3"))
                .andExpect(status().isOk());

        ArgumentCaptor<UserQuizParams> params = ArgumentCaptor.forClass(UserQuizParams.class);
        verify(quizService).getQuizByCategoryAndParams(eq(7L), params.capture());
        assertEquals(2L, params.getValue().getQuizType());
        assertEquals(5, params.getValue().getQuestionCount());
        assertEquals(1, params.getValue().getComplexityFrom());
        assertEquals(3, params.getValue().getComplexityTo());
    }
}
