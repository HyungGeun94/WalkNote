package be.stepnote.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);     // 기본 스레드 수
        executor.setMaxPoolSize(10);     // 최대 스레드 수
        executor.setQueueCapacity(100);  // 대기 큐
        executor.setThreadNamePrefix("Async-FCM-");
        executor.initialize();
        return executor;
    }
}