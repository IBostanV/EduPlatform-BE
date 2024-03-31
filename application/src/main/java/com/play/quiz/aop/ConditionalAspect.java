package com.play.quiz.aop;

import java.lang.reflect.Method;
import java.util.Objects;

import com.play.quiz.aop.annotation.Conditional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class ConditionalAspect {
    private final Environment environment;

    @Around("execution(@com.play.quiz.aop.annotation.Conditional * *.*(..))")
    public Object conditionalMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        Conditional conditional = method.getAnnotation(Conditional.class);
        String property = environment.getProperty(conditional.property());
        log.debug("Property: {}, value: {}, env value: {}", conditional.property(), conditional.value(), property);

        if (conditional.matchIfMissing()) {
            if (Objects.isNull(property)) {
                return proceedingJoinPoint.proceed();
            }

            if (Objects.equals(property, conditional.value())) {
                return proceedingJoinPoint.proceed();
            }
        }

        if (StringUtils.hasLength(property) && Objects.equals(conditional.value(), property)) {
            return proceedingJoinPoint.proceed();
        }

        return null;
    }
}
