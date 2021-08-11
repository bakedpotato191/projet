package com.example.demo.services;

import java.util.List;
import java.util.Set;

import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;

public interface PostService {
	
	Post createPost(String name, String desc);
	
	boolean deletePost(Long id);
	
	Post getPostByID(Long id);
	
	Comment addComment(CommentDto comment);
	
	Comment removeComment(Comment comment);
	
	void likeThePost(Long id);

	void dislikeThePost(Long id);

	Set<Like> getFavorites();

	List<Post> getUserPosts(String username);
}
