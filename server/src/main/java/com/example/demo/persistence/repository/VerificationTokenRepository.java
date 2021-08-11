package com.example.demo.persistence.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.User;
import com.example.demo.persistence.models.VerificationToken;

@Repository
@Transactional(readOnly = true)
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    @Modifying
    @Transactional
    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Transactional
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
