package com.dogx.core.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther:hxl
 * @Date:2021/11/1-11-01 18:05
 * @Version:1.0
 */
@Configuration
public class ThreadExecutorConfig {
    @Value("${spring.task.execution.pool.core-size:5}")
    private int corePoolSize;
    @Value("${spring.task.execution.pool.max-size:20}")
    private int maxPoolSize;
    @Value("${spring.task.execution.pool.queue-capacity:1000}")
    private int queueCapacity;
    @Value("${spring.task.execution.thread-name-prefix:myThreadPool-}")
    private String namePrefix;
    @Value("${spring.task.execution.pool.keep-alive:60}")
    private int keepAliveSeconds;
    @Bean
    public ThreadPoolTaskExecutor myAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //核心线程数
        executor.setCorePoolSize(corePoolSize);
        //任务队列的大小
        executor.setQueueCapacity(queueCapacity);
        //线程前缀名
        executor.setThreadNamePrefix(namePrefix);
        //线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
