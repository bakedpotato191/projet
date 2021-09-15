package com.example.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.rest.model.User;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.dto.response.UserDto;

public interface UserService {

	UserDto getUserData(String username);

	void follow(String username);

	void unfollow(String username);

	AvatarResponse setProfilePicture(String path);
	
	CompletableFuture<List<UserDto>> getSubscriptions(String username, int pageNo, int pageSize) throws InterruptedException;

	AvatarResponse resetProfilePicture();

	AvatarResponse getProfilePicture();

	List<UserDto> getSubscribers(String username, int pageNo, int pageSize);

	boolean isAnonymous();

	User getAuthenticatedUser();
}
