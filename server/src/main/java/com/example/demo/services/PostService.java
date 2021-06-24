package com.example.demo.services;

import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Post;

public interface PostService {
	
	Post createPost(String name, String desc);
	
	boolean deletePost(Long id);
	
	Post getPostByID(Long id);
	
	Comment addComment(CommentDto comment);
	
	Comment removeComment(Comment comment);
	
	void processLike(Long id);
}
