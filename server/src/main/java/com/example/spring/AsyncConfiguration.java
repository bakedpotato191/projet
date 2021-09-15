package com.example.spring;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

	@Override
	@Bean("taskExecutor")
	public Executor getAsyncExecutor() {
		return threadPoolTaskExecutor();
	}

	private ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		var executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(50);
		executor.setThreadNamePrefix("async-");
		executor.setTaskDecorator(new SecurityContextCopyingDecorator());
		executor.initialize();
		return executor;
	}
}
