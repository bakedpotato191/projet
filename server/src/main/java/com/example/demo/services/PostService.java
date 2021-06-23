package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.repository.CommentRepository;
import com.example.demo.persistence.repository.PostRepository;

@Service
@Transactional
public class PostService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserService userService;
	
	public Comment addComment(final CommentDto comment) {
		
		var post = postRepository.findById(Long.valueOf(comment.getId()));
		
		if (post.isPresent()) {
			var comm = new Comment();
			comm.setPost(post.get());
			comm.setUtilisateur(userService.getUserFromSession());
			comm.setText(comment.getText());
			return commentRepository.save(comm);
		}
		else {
			throw new EntityNotFoundException(Post.class, "id", comment.getId());
		}		
	}
}
