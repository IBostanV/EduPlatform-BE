package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_AUTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Objects;

import com.play.quiz.ApiWebServer;
import com.play.quiz.TestAppContextInitializer;
import com.play.quiz.exception.DuplicateUserException;
import com.play.quiz.fixtures.VerificationTokenFixture;
import com.play.quiz.security.jwt.JwtProvider;
import com.play.quiz.service.VerificationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiWebServer.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {TestAppContextInitializer.class})
@ExtendWith(OutputCaptureExtension.class)
class AuthenticationControllerIT {

    @Value("${application.domain.host.url}")
    private String redirectedURL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @AfterEach
    void tearDown() {
        namedParameterJdbcTemplate.update("DELETE FROM Q_USER WHERE ACCOUNT_ID IS NOT NULL", Collections.emptyMap());
        namedParameterJdbcTemplate.update("ALTER SEQUENCE USERS_SEQ RESTART", Collections.emptyMap());
    }

    @Test
    @Sql("/scripts/add_users.sql")
    void given_valid_credentials_when_login_then_successful() throws Exception {
        String body = "{\"id\":1,\"email\":\"email@gmail.com\",\"roles\":[{\"roleId\":2,\"name\":\"ROLE_USER\"}],\"enabled\":true}";
        String content = "{\"email\":\"email@gmail.com\",\"password\":\"admin\"}";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/login")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body))
                .andExpect(result -> assertNotNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION)));
    }

    @Test
    void given_invalid_credentials_when_login_then_validation_exception() throws Exception {
        String content = "{\"email\":\"\",\"password\":\"\"}";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/login")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("default message [password]]; default message [must not be empty]")))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("default message [email]]; default message [must not be blank]")))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("Please provide a valid email address")));
    }

    @Test
    @Sql("/scripts/add_users.sql")
    void given_wrong_password_when_login_then_bad_credentials_exception(final CapturedOutput output) throws Exception {
        String content = "{\"email\":\"email@gmail.com\",\"password\":\"no_password\"}";
        String passwordNotMatchMessage = "Failed to authenticate since password does not match stored value";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/login")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCredentialsException))
                .andExpect(content().string("Bad credentials"))
                .andExpect(result -> Assertions.assertTrue(output.getOut().contains(passwordNotMatchMessage)));
    }

    @Test
    void given_null_content_when_login_then_bad_credentials_exception(final CapturedOutput output) throws Exception {
        String missingBody = "Required request body is missing";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/login")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(missingBody)))
                .andExpect(result -> assertTrue(output.getOut().contains(missingBody)));
    }

    @Test
    void given_valid_credentials_when_register_then_user_registered(final CapturedOutput output) throws Exception {
        String username = "vanyok93@yahoo.com";
        String content = "{\"email\":\"" + username + "\",\"password\":\"Kettle9-blue\"}";
        String bodyMessage = "{\"id\":1,\"email\":\"vanyok93@yahoo.com\",\"roles\":[{\"roleId\":2,\"name\":\"ROLE_USER\"}],\"enabled\":false}";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/register")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(bodyMessage))
                .andExpect(result -> Assertions.assertTrue(output.getOut().contains("Authenticated user")))
                .andExpect(result -> assertTrue(Objects.nonNull(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION))))
                .andExpect(result -> assertEquals(jwtProvider.getUsernameFromToken(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION)), username));
    }

    @Test
    void given_invalid_credentials_when_register_then_validation_exception_thrown() throws Exception {
        String content = "{\"email\":\"\",\"password\":\"\"}";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/register")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("default message [password]]; default message [must not be empty]")))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("default message [email]]; default message [must not be blank]")))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                        .getMessage().contains("Please provide a valid email address")));
    }

    @Test
    @Sql("/scripts/add_users.sql")
    void given_same_email_when_register_then_validation_exception_thrown() throws Exception {
        String content = "{\"email\":\"email@gmail.com\",\"password\":\"Qwerty999\"}";

        mockMvc.perform(post(REQUEST_MAPPING_AUTH + "/register")
                        .with(csrf())
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateUserException))
                .andExpect(result -> assertEquals("User email@gmail.com already exists", Objects.requireNonNull(result.getResolvedException())
                        .getMessage()));
    }

    @Test
    @Sql("/scripts/add_users.sql")
    void given_activation_token_when_activateAccount_then_redirected_successfully() throws Exception {
        String token = "verification_token";

        verificationTokenService.save(VerificationTokenFixture.getVerificationToken());

        mockMvc.perform(get(REQUEST_MAPPING_AUTH + "/activate-account")
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> assertEquals(result.getResponse().getHeader("Location"), redirectedURL))
                .andExpect(result -> assertEquals(result.getResponse().getRedirectedUrl(), redirectedURL));
    }

    @Test
    void given_invalid_activation_token_when_activateAccount_then_redirected_fails() throws Exception {
        String token = "invalid_verification_token";

        mockMvc.perform(get(REQUEST_MAPPING_AUTH + "/activate-account")
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .param("token", token))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertNull(result.getResponse().getHeader("Location")))
                .andExpect(result -> assertNull(result.getResponse().getRedirectedUrl()));
    }

    @Test
    void when_createToken_then_return_new_token() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING_AUTH + "/create-token")
                        .accept(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(result -> {
                    assertTrue(result.getResponse().getContentAsString().contains("token\""));
                    assertTrue(result.getResponse().getContentAsString().contains("\"headerName\" : \"X-CSRF-TOKEN\""));
                    assertTrue(result.getResponse().getContentAsString().contains("\"parameterName\" : \"_csrf\""));
                });
    }
}
