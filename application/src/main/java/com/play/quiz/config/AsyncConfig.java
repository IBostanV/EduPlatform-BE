package com.play.quiz.config;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {

    // What the clock says, as a bean. The conquest rounds are worked out from the time rather
    // than opened by a job, so the thing that reads the time has to be replaceable or none of it
    // can be tested on a day other than the one the test was written on.
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(25);
        threadPoolTaskExecutor.setThreadNamePrefix("TaskExecutor-");
        // The request id and user follow the work onto the pool thread, so what an @Async
        // method logs can still be traced back to the request that started it; the request's
        // language too, so what it writes for the player (the activation email) is in theirs.
        threadPoolTaskExecutor.setTaskDecorator(runnable -> {
            final Map<String, String> context = ThreadContext.getImmutableContext();
            final LocaleContext locale = LocaleContextHolder.getLocaleContext();
            return () -> {
                ThreadContext.putAll(context);
                LocaleContextHolder.setLocaleContext(locale);
                try {
                    runnable.run();
                } finally {
                    ThreadContext.clearMap();
                    LocaleContextHolder.resetLocaleContext();
                }
            };
        });
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
