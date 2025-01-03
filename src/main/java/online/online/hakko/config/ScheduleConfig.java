package online.online.hakko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class ScheduleConfig {

    // TODO: 구성 설정 변경 필요
    private static final int DEFAULT_MAX_POOL_SIZE = 3;
    private static final String SCHEDULER_THREAD_PREFIX = "ThreadPoolTaskScheduler";
    private static final String EXECUTOR_THREAD_PREFIX = "ThreadPoolTaskExecutor";

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(DEFAULT_MAX_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(SCHEDULER_THREAD_PREFIX);
        return threadPoolTaskScheduler;
    }

    @Bean
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
        threadPoolTaskExecutor.setThreadNamePrefix(EXECUTOR_THREAD_PREFIX);
        return threadPoolTaskExecutor;
    }
}
