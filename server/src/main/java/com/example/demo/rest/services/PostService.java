package com.example.demo.rest.services;

import java.util.List;

import com.example.demo.rest.dto.CommentDto;
import com.example.demo.rest.models.Comment;
import com.example.demo.rest.models.Like;
import com.example.demo.rest.models.Post;

public interface PostService {
	
	Post createPost(String name, String desc);
	
	void deletePost(Long id);
	
	Post getPostByID(Long id);
	
	Comment addComment(CommentDto comment);
	
	Comment removeComment(Comment comment);
	
	void likeThePost(Long id);

	void dislikeThePost(Long id);

	List<Like> getFavorites(Integer pageNo, Integer pageSize, String sortBy);

	List<Post> getUserPosts(String username, Integer pageNo, Integer pageSize, String sortBy);
}
