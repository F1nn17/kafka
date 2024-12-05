package com.shiraku.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Количество потоков для обработки
        executor.setMaxPoolSize(10); // Максимальное количество потоков
        executor.setQueueCapacity(25); // Размер очереди задач
        executor.setThreadNamePrefix("PaymentAsyncThread-");
        executor.initialize();
        return executor;
    }
}
