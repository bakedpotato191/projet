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
	@Mapping(target = "roles", ignore = true)
	@Mapping(target="posts", ignore=true)
	User userImplToUser(UserDetailsImpl user);
	
	List<PostDto> postListToPostListDto(List<Post> post);
	
}
