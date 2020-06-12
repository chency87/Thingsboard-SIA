package org.thingsboard.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class GlobalConfig {

    @Bean
    public ThreadPoolTaskExecutor defaultThreadPool(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数量
        threadPoolTaskExecutor.setCorePoolSize(4);
        //最大线程数量
        threadPoolTaskExecutor.setMaxPoolSize(8);
        //队列中最大任务数
        threadPoolTaskExecutor.setQueueCapacity(8);
        //线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("Async-Plugin-ThreadPool-");
        //当达到最大线程数时如何处理新任务
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后最大存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        //初始化线程池
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
