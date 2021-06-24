package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findAllByPostOrderByDateDesc(Post post);
}
