package com.play.quiz.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// One line per HTTP request, and a request id in every line logged while serving it, so all
// that one call did can be grepped out of the log. The user is put in by JwtAuthenticationFilter
// once the token is read. The id goes back in X-Request-Id for matching a client report to the log.
@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID = "requestId";
    public static final String USER = "user";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final Pattern SECRET_PARAM = Pattern.compile("(?i)([^&=]*(?:token|password|secret)[^&=]*)=[^&]*");

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String requestId = UUID.randomUUID().toString().substring(0, 8);
        ThreadContext.put(REQUEST_ID, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);
        final long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            final int status = response.getStatus();
            final String line = "{} {}{} -> {} in {}ms ip={} ua=\"{}\"";
            final Object[] params = {request.getMethod(), request.getRequestURI(),
                    Optional.ofNullable(request.getQueryString()).map(query -> "?" + SECRET_PARAM.matcher(query).replaceAll("$1=***")).orElse(""),
                    status, System.currentTimeMillis() - start, clientIp(request), request.getHeader("User-Agent")};
            if (status >= 500) {
                log.error(line, params);
            } else if (status >= 400) {
                log.warn(line, params);
            } else {
                log.info(line, params);
            }
            ThreadContext.clearMap();
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }

    private static String clientIp(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(forwarded -> forwarded.split(",")[0].trim())
                .orElse(request.getRemoteAddr());
    }
}
