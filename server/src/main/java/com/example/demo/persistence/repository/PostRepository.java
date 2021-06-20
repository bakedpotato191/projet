package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByUtilisateurOrderByDateDesc(User user);
	
	@Override
	void delete(Post post);
}
