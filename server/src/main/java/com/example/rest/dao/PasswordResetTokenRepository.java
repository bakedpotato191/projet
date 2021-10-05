package com.example.rest.dao;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.PasswordResetToken;
import com.example.rest.model.User;

@Repository
@Transactional(readOnly = true)
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    @Async
    @Modifying
    @Transactional
    void deleteByExpiryDateLessThan(Date now);

    @Async
    @Modifying
    @Transactional
    CompletableFuture<Void> deleteAllByExpiryDateLessThan(Date now);
    
    @Modifying
    @Transactional
    void deleteAllByUser(User user);
}
