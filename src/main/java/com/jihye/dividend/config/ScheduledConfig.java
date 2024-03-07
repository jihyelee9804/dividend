package com.jihye.dividend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class ScheduledConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // thread pool 생성
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        // 코어 개수
        int n = Runtime.getRuntime().availableProcessors();
        // thread pool size setting
        threadPool.setPoolSize(n);
        // thread pool 초기화
        threadPool.initialize();

        taskRegistrar.setTaskScheduler(threadPool);
    }
}
