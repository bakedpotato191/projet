package com.example.demo.rest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.models.Comment;


@Repository
@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	Page<Comment> findAllByPostId(Long id, Pageable paging);
	
	@Modifying
	@Transactional
	@Query(	value = "DELETE c.* from comment c INNER JOIN post p on c.post_id=p.id INNER JOIN utilisateur u on c.user_id=u.id OR p.user_id=u.id WHERE c.id=:id AND u.username=:username", 
			nativeQuery = true)
	int delete(@Param("id") Long id, @Param("username") String username);
	
	
}
