package cn.hellohao.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

//线程池
@Configuration
@EnableAsync
public class PoolConfig {
    ThreadPoolProperties properties = new ThreadPoolProperties();
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize()); //设置核心线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize()); //设置最大线程数
        executor.setQueueCapacity(properties.getQueueCapacity()); //设置队列容量
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setKeepAliveSeconds(properties.getKeepAliveTime());
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        // 设置任务拒绝策略
        /**
         * 4种
         * ThreadPoolExecutor类有几个内部实现类来处理这类情况：
         - AbortPolicy 丢弃任务，抛RejectedExecutionException
         - CallerRunsPolicy 由该线程调用线程运行。直接调用Runnable的run方法运行。
         - DiscardPolicy  抛弃策略，直接丢弃这个新提交的任务
         - DiscardOldestPolicy 抛弃旧任务策略，从队列中踢出最先进入队列（最后一个执行）的任务
         * 实现RejectedExecutionHandler接口，可自定义处理器
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Data
    class ThreadPoolProperties {
        private int corePoolSize = 5;
        private int maxPoolSize = 50;
        private int keepAliveTime = 15;
        private int queueCapacity = 10;
        private String threadNamePrefix = "Tbed-Thread";
        private boolean allowCoreThreadTimeout = false;
        private boolean waitForTasksToCompleteOnShutdown = false;
        private int awaitTerminationSeconds;

    }
}