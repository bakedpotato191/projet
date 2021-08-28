package com.example.service;

import java.util.List;

import com.example.rest.model.Like;
import com.example.rest.model.Post;

public interface PostService {
	
	Post createPost(String name, String desc);
	
	void deletePost(Long id);
	
	Post getPostByID(Long id);

	void like(Long id);

	void dislike(Long id);

	List<Like> getFavorites(Integer pageNo, Integer pageSize, String sortBy);

	List<Post> getUserPosts(String username, Integer pageNo, Integer pageSize, String sortBy);
}
