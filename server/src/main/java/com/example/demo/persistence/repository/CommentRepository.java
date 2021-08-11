package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Post;

@Repository
@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findAllByPostOrderByDateDesc(Post post);
}
