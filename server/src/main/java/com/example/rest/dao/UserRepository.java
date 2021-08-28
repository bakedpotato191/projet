package com.example.rest.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.avatar=:path, u.has_avatar = 1 WHERE u=:user")
    int setProfilePicture(User user, String path);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.avatar=DEFAULT, u.has_avatar = 0 WHERE u=:user")
	void resetProfilePicture(User user);
	
	@Query("SELECT u.avatar, u.has_avatar FROM User u WHERE u=:user")
	List<Object[]> getProfilePicture(User user);
	
	@Query("SELECT COUNT(u)>0 from User u WHERE u.avatar like %:avatar")
	boolean existsByAvatar(@Param("avatar") String filename);
	
	@Override
	@Transactional
    void delete(User user);
}
