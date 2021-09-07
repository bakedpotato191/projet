package com.example.service;

import java.util.List;

import com.example.rest.model.Comment;
import com.example.web.dto.request.CommentReqDto;
import com.example.web.dto.response.CommentResDto;

public interface CommentService {

	Comment addComment(CommentReqDto comment);
	
	void deleteComment(Long id);

	List<CommentResDto> listComments(Long id, int pageNo, int pageSize, String sortBy);
}
