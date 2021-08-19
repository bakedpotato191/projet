package com.example.demo.rest.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.rest.dto.CommentDto;
import com.example.demo.rest.exceptions.EntityNotFoundException;
import com.example.demo.rest.exceptions.HttpUnauthorizedException;
import com.example.demo.rest.models.Comment;
import com.example.demo.rest.models.Post;
import com.example.demo.rest.repository.CommentRepository;
import com.example.demo.rest.repository.PostRepository;
import com.example.demo.rest.services.CommentService;
import com.example.demo.rest.services.UserService;

@Service
public class CommentServiceImpl implements CommentService {


	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	
	@Override
	public List<Comment> listComments (Long id, int pageNo, int pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Comment> slicedResult = commentRepository.findAllByPostId(id, paging);
		
		if (slicedResult.hasContent()) {
			return slicedResult.getContent();
		}
		else {
			return new ArrayList<>();
		}
		
	}
	
	@Override
	public Comment addComment(final CommentDto comment) {

		var post = postRepository.findById(comment.getId());

		if (post.isPresent()) {
			
			var comm = new Comment();
			comm.setUtilisateur(userService.getUserFromSession());
			comm.setText(comment.getText());
			comm.setDate(new Timestamp(System.currentTimeMillis()));
			comm.setPost(post.get());
			
			return commentRepository.save(comm);
		} 
		else {
			throw new EntityNotFoundException(Post.class, "id", comment.getId().toString());
		}
	}
	
	@Override
	public void deleteComment(Long id) {
		var username = userService.getUserFromSession().getUsername();	
				if (commentRepository.delete(id, username) == 0) {
				throw new HttpUnauthorizedException("The comment does not exist or you do not have the permission to access it");
		}
	}

}
