package com.example.service;

import java.util.Map;

import com.example.rest.model.Comment;
import com.example.web.dto.request.CommentDto;

public interface CommentService {

	Comment addComment(CommentDto comment);
	
	void deleteComment(Long id);

	Map<String, Object> listComments(Long id, int pageNo, int pageSize, String sortBy);
}
