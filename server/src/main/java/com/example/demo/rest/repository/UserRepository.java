package com.example.demo.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.models.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.avatar=:path WHERE u=:user")
    int setProfilePicture(User user, String path);
	
	@Override
	@Transactional
    void delete(User user);
}
