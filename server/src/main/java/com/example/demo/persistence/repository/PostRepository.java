package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByUtilisateurOrderByDateDesc(User user);
	
	Long countByUtilisateur(User user);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Post p WHERE p.id = ?1")
	void deleteById(Long id);
}
