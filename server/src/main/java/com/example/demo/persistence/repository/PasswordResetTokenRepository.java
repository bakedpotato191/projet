package com.example.demo.persistence.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.PasswordResetToken;
import com.example.demo.persistence.models.User;

@Repository
@Transactional(readOnly = true)
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {


    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    @Transactional
    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Transactional
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
