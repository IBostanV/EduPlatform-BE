package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_GLOSSARY;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.play.quiz.ApiWebServer;
import com.play.quiz.TestAppContextInitializer;
import com.play.quiz.dto.CategoryDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.fixtures.CategoryFixture;
import com.play.quiz.fixtures.GlossaryFixture;
import com.play.quiz.service.CategoryService;
import com.play.quiz.service.GlossaryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiWebServer.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {TestAppContextInitializer.class})
class GlossaryControllerIT {

    @Value("${application.security.jwt.token}")
    private String token;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private GlossaryService glossaryService;

    @Autowired
    private CategoryService categoryService;

    @AfterEach
    void tearDown() {
        namedParameterJdbcTemplate.update("DELETE FROM Q_GLOSSARY WHERE TERM_ID IS NOT NULL", Collections.emptyMap());
        namedParameterJdbcTemplate.update("ALTER SEQUENCE GLOSSARIES_SEQ RESTART", Collections.emptyMap());

        namedParameterJdbcTemplate.update("DELETE FROM Q_CATEGORY WHERE CAT_ID IS NOT NULL", Collections.emptyMap());
        namedParameterJdbcTemplate.update("ALTER SEQUENCE CATEGORIES_SEQ RESTART", Collections.emptyMap());

        namedParameterJdbcTemplate.update("DELETE FROM Q_USER WHERE ACCOUNT_ID IS NOT NULL", Collections.emptyMap());
        namedParameterJdbcTemplate.update("ALTER SEQUENCE USERS_SEQ RESTART", Collections.emptyMap());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_glossaryId_when_getGlossaryById_then_return_glossary() throws Exception {
        long glossaryId = 1L;
        String body = "{\"termId\":1,\"value\":\"Canberra\",\"isActive\":true,\"categoryName\":\"Continent\",\"key\":\"Australia\",\"categoryId\":1}";

        CategoryDto categoryDto = categoryService.save(CategoryFixture.getNoIdCategoryDto(), null);
        glossaryService.save(GlossaryFixture.getActiveGlossaryNoIdWithCategoryDto(categoryDto), null);

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/id/" + glossaryId)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_null_when_getGlossaryById_then_throw_MethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/id/" + null)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertInstanceOf(MethodArgumentTypeMismatchException.class, result.getResolvedException()))
                .andExpect(content().string("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"null\""));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_non_existing_glossaryId_when_getGlossaryById_then_throw_RecordNotFoundException() throws Exception {
        long glossaryId = 2;

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/id/" + glossaryId)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertInstanceOf(RecordNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string("This could not be found. It may have been deleted."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_glossaryKey_when_getGlossaryByKey_then_return_glossary() throws Exception {
        String key = "Australia";
        String body = "{\"termId\":1,\"value\":\"Canberra\",\"isActive\":true,\"categoryName\":\"Continent\",\"key\":\"Australia\",\"categoryId\":1}";

        CategoryDto categoryDto = categoryService.save(CategoryFixture.getNoIdCategoryDto(), null);
        glossaryService.save(GlossaryFixture.getActiveGlossaryNoIdWithCategoryDto(categoryDto), null);

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/key/" + key)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_null_when_getGlossaryByKey_then_throw_MethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/key/" + null)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertInstanceOf(RecordNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string("No records found by key: null"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_non_existing_glossaryKey_when_getGlossaryByKey_then_throw_RecordNotFoundException() throws Exception {
        String key = "SUA";

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/key/" + key)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertInstanceOf(RecordNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string("No records found by key: SUA"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_categoryId_when_getGlossaryByCategoryId_then_return_glossary() throws Exception {
        long categoryId = 1;
        String body = "[{\"termId\":1,\"value\":\"Canberra\",\"isActive\":true,\"categoryName\":\"Continent\",\"key\":\"Australia\",\"categoryId\":2}]";

        categoryService.save(CategoryFixture.getParentNoIdDto(), null);
        CategoryDto categoryDto = categoryService.save(CategoryFixture.getNoIdParentCategoryDto(), null);
        glossaryService.save(GlossaryFixture.getActiveGlossaryNoIdWithCategoryDto(categoryDto), null);

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/category/" + categoryId)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql({"/scripts/add_users.sql"})
    void given_null_when_getGlossaryByCategoryId_then_throw_MethodArgumentTypeMismatchException() throws Exception {
        CategoryDto categoryDto = categoryService.save(CategoryFixture.getNoIdCategoryDto(), null);
        glossaryService.save(GlossaryFixture.getActiveGlossaryNoIdWithCategoryDto(categoryDto), null);

        mockMvc.perform(MockMvcRequestBuilders.get(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/category/" + null)
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertInstanceOf(MethodArgumentTypeMismatchException.class, result.getResolvedException()))
                .andExpect(content().string("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"null\""));
    }

}
