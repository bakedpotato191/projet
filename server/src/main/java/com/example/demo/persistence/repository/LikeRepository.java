package com.example.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

	boolean findByUtilisateurAndPost(User user, Post post);
}
