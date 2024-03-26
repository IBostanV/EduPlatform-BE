package com.play.quiz.config;

import static com.play.quiz.controller.RestEndpoint.CONTEXT_PATH;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_AUTH;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CATEGORY;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_LANGUAGE;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_TRANSLATION;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import com.play.quiz.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
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
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(REQUEST_MAPPING_AUTH + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_CATEGORY + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_LANGUAGE + "/**").permitAll()
                        .requestMatchers(CONTEXT_PATH + REQUEST_MAPPING_TRANSLATION + "/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
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
