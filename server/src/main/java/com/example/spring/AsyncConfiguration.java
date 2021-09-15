package com.example.spring;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import com.example.web.exception.AsyncExceptionHandler;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
	
	@Override
	public Executor getAsyncExecutor() {
		var executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(50);
		executor.setThreadNamePrefix("async-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	    return new AsyncExceptionHandler();
	}

}
