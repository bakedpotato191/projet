package com.example.demo.mapstruct.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.mapstruct.dto.PostDto;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.models.User;
import com.example.demo.services.UserDetailsImpl;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
	
	@Mapping(target = "nom", ignore = true)
	@Mapping(target = "prenom", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "likes", ignore = true)
	@Mapping(target = "postCount", ignore = true)
	@Mapping(target = "avatar", ignore = true)
	@Mapping(target = "posts", ignore = true)
	@Mapping(target = "roles", ignore = true)
	User userImplToUser(UserDetailsImpl user);
	
	List<PostDto> postListToPostListDto(List<Post> post);
	
}
