package com.example.demo.rest.services;

import java.util.Map;

import com.example.demo.rest.dto.CommentDto;
import com.example.demo.rest.models.Comment;

public interface CommentService {

	Comment addComment(CommentDto comment);
	
	void deleteComment(Long id);

	Map<String, Object> listComments(Long id, int pageNo, int pageSize, String sortBy);
}
