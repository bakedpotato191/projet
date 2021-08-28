package com.example.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.CommentRepository;
import com.example.rest.dao.PostRepository;
import com.example.rest.model.Comment;
import com.example.rest.model.Post;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.web.dto.request.CommentDto;
import com.example.web.exception.EntityNotFoundException;
import com.example.web.exception.HttpUnauthorizedException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {


	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	
	@Override
	public Map<String, Object> listComments (Long id, int pageNo, int pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		var slicedResult = commentRepository.findAllByPostId(id, paging);
		
		if (slicedResult.hasContent()) {
			Map<String, Object> ret = new HashMap<>();
			ret.put("total_pages", slicedResult.getTotalPages());
			ret.put("total_elements", slicedResult.getTotalElements());
			ret.put("comments", slicedResult.getContent());
			return ret;
		}
		else {
			return new HashMap<>();
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
