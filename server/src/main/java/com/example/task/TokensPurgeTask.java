package com.example.task;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.PasswordResetTokenRepository;
import com.example.rest.dao.VerificationTokenRepository;

@Service
@Transactional
public class TokensPurgeTask {

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Scheduled(cron = "${cron.expression.tokens}", zone="Europe/Paris")
    public void purgeExpired() throws InterruptedException, ExecutionException {
		var now = Date.from(Instant.now());
        var future1 = CompletableFuture.supplyAsync(() -> passwordTokenRepository.deleteAllByExpiryDateLessThan(now));
        var future2 = CompletableFuture.supplyAsync(() -> tokenRepository.deleteAllByExpiryDateLessThan(now));
        CompletableFuture<Void> combined = CompletableFuture.allOf(future1, future2);
        combined.get();
    }
}
