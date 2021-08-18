package com.example.demo.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

	Slice<Post> findAllByUtilisateurUsername(String username, Pageable paging);
	
	Long countByUtilisateur(User user);
	
	@Modifying
	@Transactional
	int deleteByIdAndUtilisateur(@Param("id") Long id, @Param("user") User user);
}
