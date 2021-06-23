package com.example.demo.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	Set<Comment> findAllByPostOrderByDateDesc(Post post);
}
