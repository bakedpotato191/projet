package com.example.demo.services;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.repository.CommentRepository;
import com.example.demo.persistence.repository.LikeRepository;
import com.example.demo.persistence.repository.PostRepository;

@Service
@Transactional
public class PhotoService {
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment addComment(final CommentDto comment) {
		
		var post = postRepository.findById(Long.valueOf(comment.getId()));
		
		if (post.isPresent()) {
			var comm = new Comment();
			comm.setUtilisateur(userService.getUserFromSession());
			comm.setText(comment.getText());
			comm.setDate(new Timestamp(System.currentTimeMillis()));
			comm.setPost(post.get());
			return commentRepository.save(comm);
		}
		else {
			throw new EntityNotFoundException(Post.class, "id", comment.getId());
		}		
	}
	
	public Like likePhoto(final String id) {
		
		var post = postRepository.findById(Long.valueOf(id));
		
		if (post.isPresent()) {
			var like = new Like();
			like.setUtilisateur(userService.getUserFromSession());
			like.setPost(post.get());
			
			return likeRepository.save(like);
		}
		else {
			throw new EntityNotFoundException(Post.class, "id", id);
		}		
	}
}
