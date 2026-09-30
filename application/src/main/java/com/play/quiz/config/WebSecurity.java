package com.play.quiz.config;

import static com.play.quiz.controller.RestEndpoint.CONTEXT_PATH;
import static com.play.quiz.controller.RestEndpoint.QUIZ_CUSTOM;
import static com.play.quiz.controller.RestEndpoint.QUIZ_TYPES;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_AUTH;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CATEGORY;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CONQUEST;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_FEED;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_LEADERBOARD;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_FEEDBACK;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_GLOSSARY;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_KNOWLEDGE_BASE;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_LANGUAGE;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_QUESTION;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_QUIZ;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_TRANSLATION;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_USER;
import static com.play.quiz.controller.RestEndpoint.USER_ADMIN;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import com.play.quiz.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurity {
    // hasRole("ADMIN") matches the authority "ROLE_ADMIN" (UserRole.ROLE_ADMIN) given at login.
    private static final String ADMIN = "ADMIN";

    // The quiz content — categories, glossaries, questions, knowledge base — is the content
    // dashboard's, and the two content roles work in it alongside admins. Everything else that
    // is admin-only (the accounts, what players send in) stays hasRole(ADMIN).
    // ponytail: a publisher may do everything an editor may; split them when "editor drafts,
    // publisher makes visible" is actually wanted.
    private static final String[] CONTENT = {ADMIN, "CONTENT_EDITOR", "CONTENT_PUBLISHER"};

    @Value("#{'${application.security.allowed-origins}'.split(',')}")
    private final List<String> allowedOrigins;
    @Value("#{'${application.security.exposed-headers}'.split(',')}")
    private final List<String> exposedHeaders;

    @Qualifier("quizUserDetailsService")
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter authenticationFilter;
    private final CsrfTokenRequestHandler customCsrfTokenRequestAttributeHandler;

    @Bean
    protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .logout(logout -> logout.deleteCookies(HttpHeaders.AUTHORIZATION.toLowerCase())
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(customCsrfTokenRequestAttributeHandler))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // First match wins, so each admin-only rule sits above the broader rule for its path.
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(REQUEST_MAPPING_AUTH + "/**").permitAll()
                        // Every custom quiz at once is a moderation view, so admins only.
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUIZ + QUIZ_CUSTOM + "/all").hasRole(ADMIN)
                        // Building a quiz needs an account: it is saved under its creator and the
                        // invites go out in their name. Above the open rule, which matches first.
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUIZ + QUIZ_CUSTOM,
                                CONTEXT_PATH + REQUEST_MAPPING_QUIZ + QUIZ_CUSTOM + "/**").authenticated()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUIZ + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_LANGUAGE + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_TRANSLATION + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUIZ + QUIZ_TYPES).permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                        // Categories: anyone may read them (home carousel, Take quiz); creating,
                        // editing and deleting one is the content dashboard's.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_CATEGORY + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_CATEGORY + "/**").hasAnyRole(CONTENT)

                        // Knowledge base: `all-records` includes drafts and hidden records, and PATCH
                        // on the root saves one; both belong to the content dashboard. Reading
                        // published articles and voting on them stays open to everyone.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_KNOWLEDGE_BASE + "/all-records").hasAnyRole(CONTENT)
                        .requestMatchers(HttpMethod.PATCH, CONTEXT_PATH + REQUEST_MAPPING_KNOWLEDGE_BASE + "/**").hasAnyRole(CONTENT)
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_KNOWLEDGE_BASE + "/**").permitAll()

                        // Glossaries: signed-in users may read them; writing one is content work,
                        // and so is the dashboard's count of terms still missing a type.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/missing-type-count").hasAnyRole(CONTENT)
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/**").authenticated()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY + "/**").hasAnyRole(CONTENT)

                        // Questions: the dashboard listings and every write are content work. Players
                        // still read a question and its answers while taking a quiz.
                        .requestMatchers(HttpMethod.GET,
                                CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/page",
                                CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/all-questions").hasAnyRole(CONTENT)
                        // The home page mini game is public.
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/mini-game",
                                CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/mini-game/*/check").permitAll()
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/**").authenticated()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_QUESTION + "/**").hasAnyRole(CONTENT)

                        // Feedback: anyone may send one, guests included (rate-limited in the
                        // controller); reading and resolving them is admin-only.
                        .requestMatchers(HttpMethod.POST, CONTEXT_PATH + REQUEST_MAPPING_FEEDBACK).permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_FEEDBACK,
                                CONTEXT_PATH + REQUEST_MAPPING_FEEDBACK + "/**").hasRole(ADMIN)

                        // Conquest: the map reads for everyone, so a guest sees who holds what
                        // and what the game is. Entering a run needs an account.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_CONQUEST).permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_CONQUEST + "/**").authenticated()

                        // News reads for everyone (a guest just gets no friends in it). Posting
                        // and deleting need an account, like the notifications, and fall to the
                        // rule at the end; FeedService decides patch note or friend post, and who
                        // may delete what.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_FEED + "/news").permitAll()

                        // The leaderboards are the home page's, so a guest reads them too.
                        .requestMatchers(HttpMethod.GET, CONTEXT_PATH + REQUEST_MAPPING_LEADERBOARD).permitAll()

                        // Managing the accounts themselves: emails, roles, blocking, deleting.
                        // The rest of /user is the signed-in player's own account, below.
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_USER + USER_ADMIN,
                                CONTEXT_PATH + REQUEST_MAPPING_USER + USER_ADMIN + "/**").hasRole(ADMIN)

                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/errors/access-denied")
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .sessionConcurrency(sessionConcurrency ->
                                        sessionConcurrency.expiredUrl("/login?expired")
                                )
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter, LogoutFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        handleGlobalAuthenticationConfigurerAdapter();
        return authenticationConfiguration.getAuthenticationManager();
    }

    private void handleGlobalAuthenticationConfigurerAdapter() {
        new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
                authenticationManagerBuilder
                        .userDetailsService(userDetailsService)
                        .passwordEncoder(passwordEncoder());
            }
        };
    }
}
