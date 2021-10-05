package com.example.rest.dao;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.User;
import com.example.rest.model.VerificationToken;

@Repository
@Transactional(readOnly = true)
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    @Modifying
    @Transactional
    void deleteByExpiryDateLessThan(Date now);
    
    @Async
    @Modifying
    @Transactional
    CompletableFuture<Void> deleteAllByExpiryDateLessThan(Date now);
}
