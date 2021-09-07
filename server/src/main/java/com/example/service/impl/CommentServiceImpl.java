package com.example.service.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.CommentRepository;
import com.example.rest.dao.PublicationRepository;
import com.example.rest.model.Comment;
import com.example.rest.model.Publication;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.web.dto.request.CommentReqDto;
import com.example.web.dto.response.CommentResDto;
import com.example.web.exception.EntityNotFoundException;
import com.example.web.exception.HttpUnauthorizedException;
import com.example.web.mappers.MapstructMapper;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {


	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MapstructMapper mapper;
	
	
	@Override
	public List<CommentResDto> listComments (Long id, int pageNo, int pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Comment> slicedResult = commentRepository.findAllByPostId(id, paging);
		
		if (slicedResult.hasContent()) {
			return mapper.commListMap(slicedResult.getContent());
		}
		else {
			return Collections.emptyList();
		}
		
	}
	
	@Override
	public Comment addComment(final CommentReqDto comment) {

		var post = publicationRepository.findById(comment.getPost_id());
		if (post.isPresent()) {
			
			var comm = new Comment();
			comm.setUtilisateur(userService.getUserFromSession());
			comm.setText(comment.getComment());
			comm.setDate(new Timestamp(System.currentTimeMillis()));
			comm.setPost(post.get());
			
			return commentRepository.save(comm);
		} 
		else {
			throw new EntityNotFoundException(Publication.class, "id", comment.getPost_id().toString());
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
