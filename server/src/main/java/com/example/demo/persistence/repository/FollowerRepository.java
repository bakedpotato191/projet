package com.example.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.models.Follower;
import com.example.demo.persistence.models.User;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

	@Modifying
	@Query(value = "DELETE from Follower f WHERE f.from = :user AND f.to.username = :username")
	int unfollow(@Param("user") User user, @Param("username") String username);
	
	@Query("SELECT COUNT(f)>0 from Follower f WHERE f.from = :follower AND f.to = :following")
	boolean isFollowed(@Param ("follower") User from, @Param("following") User to);
}
