package com.example.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.example.rest.model.Publication;
import com.example.web.dto.response.PublicationDto;

public interface PublicationService {
	
	Publication createPost(String name, String desc);
	
	void deletePublication(Long id);
	
	PublicationDto getPublicationByID(Long id);

	void like(Long id);

	void dislike(Long id);

	CompletableFuture<List<PublicationDto>> getFavorites(Integer pageNo, Integer pageSize, String sortBy);

	Map<String, Object> getUserPublications(String username, Integer pageNo, Integer pageSize, String sortBy);

	CompletableFuture<List<PublicationDto>> getNewPublications(Integer pageNo, Integer pageSize);

}
