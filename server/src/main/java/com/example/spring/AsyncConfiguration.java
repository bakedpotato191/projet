package com.example.spring;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

@Configuration
public class AsyncConfiguration extends AsyncConfigurerSupport {

	@Override
	  public Executor getAsyncExecutor() {
	    return new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
	  }


}
