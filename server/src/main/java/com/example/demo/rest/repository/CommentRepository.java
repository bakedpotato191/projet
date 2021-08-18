package com.example.demo.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.models.Comment;
import com.example.demo.rest.models.Post;

@Repository
@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findAllByPostOrderByDateDesc(Post post);
}
