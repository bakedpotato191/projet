package com.example.service;

import java.util.List;

import com.example.rest.model.Publication;
import com.example.web.dto.response.FavoriDto;
import com.example.web.dto.response.PublicationDto;

public interface PostService {
	
	Publication createPost(String name, String desc);
	
	void deletePost(Long id);
	
	PublicationDto getPostByID(Long id);

	void like(Long id);

	void dislike(Long id);

	List<FavoriDto> getFavorites(Integer pageNo, Integer pageSize, String sortBy);

	List<PublicationDto> getUserPosts(String username, Integer pageNo, Integer pageSize, String sortBy);
}
