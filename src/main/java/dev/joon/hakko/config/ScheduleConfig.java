package dev.joon.hakko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    private static final int DEFAULT_MAX_POOL_SIZE = 1;
    private static final String SCHEDULER_THREAD_PREFIX = "ThreadPoolTaskScheduler";

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(DEFAULT_MAX_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(SCHEDULER_THREAD_PREFIX);
        return threadPoolTaskScheduler;
    }
}
