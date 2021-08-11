package com.example.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByUtilisateurUsernameOrderByDateDesc(String username);
	
	Long countByUtilisateur(User user);
	
	@Modifying
	@Transactional
	int deleteByIdAndUtilisateur(@Param("id") Long id, @Param("user") User user);
}
