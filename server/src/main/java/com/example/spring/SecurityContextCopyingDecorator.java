package com.example.spring;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextCopyingDecorator implements TaskDecorator {

	@Override
    public Runnable decorate(Runnable runnable) {
      final var a = SecurityContextHolder.getContext().getAuthentication();
      return () -> {
        try {
        	var ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(a);
            SecurityContextHolder.setContext(ctx);
          runnable.run();
        } finally {
            SecurityContextHolder.clearContext();
        }
      };
    }
}