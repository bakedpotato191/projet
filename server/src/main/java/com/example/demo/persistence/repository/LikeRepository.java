package com.example.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

	@Query("select count(l)>0 from Like l where l.utilisateur = :user AND post = :post")
	boolean isLiked(@Param ("user") User user, @Param("post") Post post);
	
	@Query("select count(utilisateur) from Like l where l.post = :post")
	int countLikes(@Param ("post") Post post);
	
	Long countByPost(Post post);
	
	@Modifying
	@Query(value = "delete from Like l where l.utilisateur=:user and l.post=:post")
	int dislike(@Param("user") User user, @Param("post") Post post);
}
