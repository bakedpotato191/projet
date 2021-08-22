package com.example.demo.tasks;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.repository.PasswordResetTokenRepository;
import com.example.demo.rest.repository.VerificationTokenRepository;

@Service
@Transactional
public class TokensPurgeTask {

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {

        var now = Date.from(Instant.now());

        passwordTokenRepository.deleteAllExpiredSince(now);
        tokenRepository.deleteAllExpiredSince(now);
    }
}
