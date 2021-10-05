package com.example.rest.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.Favori;
import com.example.rest.model.Publication;
import com.example.rest.model.User;

@Repository
@Transactional(readOnly = true)
public interface FavoriRepository extends JpaRepository<Favori, Long> {

	@Query("SELECT COUNT(f)>0 from Favori f WHERE f.utilisateur = (:user) AND post = (:post)")
	boolean isLiked(@Param ("user") User user, @Param("post") Publication post);
	
	@Modifying
	@Transactional
	@Query("DELETE from Favori f WHERE f.utilisateur=(:user) AND f.post.id=(:id)")
	void dislike(@Param("user") User user, @Param("id") Long id);
	
	Slice<Favori> findAllByUtilisateurUsername(String username, Pageable paging);
	
	int countByPost(Publication post);
}
