package com.play.quiz.aop;

import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.play.quiz.config.RequestLoggingFilter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

// What every user did: each controller call with its arguments, how long it took, and what it
// threw. Also covers the STOMP handlers, which run outside the HTTP filter and so get their user
// here from the Principal. Arguments are cut short; passwords are char[] and print as a reference.
@Log4j2
@Aspect
@Component
public class ControllerLoggingAspect {

    private static final int MAX_ARG_LENGTH = 300;
    private static final Pattern SECRET = Pattern.compile("(?i)token|password|secret");

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logAction(final ProceedingJoinPoint joinPoint) throws Throwable {
        final boolean ownsUser = !ThreadContext.containsKey(RequestLoggingFilter.USER);
        if (ownsUser) {
            Arrays.stream(joinPoint.getArgs())
                    .filter(Principal.class::isInstance)
                    .findFirst()
                    .ifPresent(principal -> ThreadContext.put(RequestLoggingFilter.USER, ((Principal) principal).getName()));
        }
        final String action = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
        log.info("{} args=[{}]", action, describe((CodeSignature) joinPoint.getSignature(), joinPoint.getArgs()));
        final long start = System.currentTimeMillis();
        try {
            final Object result = joinPoint.proceed();
            log.debug("{} done in {}ms", action, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable throwable) {
            log.warn("{} failed in {}ms with {}: {}", action, System.currentTimeMillis() - start,
                    throwable.getClass().getSimpleName(), throwable.getMessage());
            throw throwable;
        } finally {
            if (ownsUser) {
                ThreadContext.remove(RequestLoggingFilter.USER);
            }
        }
    }

    private static String describe(final CodeSignature signature, final Object[] args) {
        final String[] names = signature.getParameterNames();
        return IntStream.range(0, args.length)
                .filter(i -> !(args[i] instanceof Principal || args[i] instanceof ServletRequest
                        || args[i] instanceof ServletResponse || args[i] instanceof BindingResult))
                .mapToObj(i -> {
                    final String name = names == null ? "arg" + i : names[i];
                    return name + "=" + describe(name, args[i]);
                })
                .collect(Collectors.joining(", "));
    }

    private static String describe(final String name, final Object arg) {
        if (SECRET.matcher(name).find()) {
            return "***";
        }
        final String text = Objects.toString(arg);
        return text.length() > MAX_ARG_LENGTH ? text.substring(0, MAX_ARG_LENGTH) + "..." : text;
    }
}
