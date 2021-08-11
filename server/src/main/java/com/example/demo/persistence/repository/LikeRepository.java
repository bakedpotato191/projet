package com.example.demo.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

@Repository
@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Like, Long> {

	@Query("SELECT COUNT(l)>0 from Like l WHERE l.utilisateur = :user AND post = :post")
	boolean isLiked(@Param ("user") User user, @Param("post") Post post);
	
	@Query("SELECT COUNT(utilisateur) FROM Like l WHERE l.post = :post")
	int countLikes(@Param ("post") Post post);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE from Like l WHERE l.utilisateur=:user AND l.post.id=:id")
	int dislike(@Param("user") User user, @Param("id") Long id);
	
	Set<Like> findAllByUtilisateurOrderByPostDateDesc(User user);
	
	Long countByPost(Post post);
}
