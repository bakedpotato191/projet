package com.example.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.example.rest.model.User;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.dto.response.UserDto;

public interface UserService {

	UserDto getUserData(String username) throws InterruptedException, ExecutionException;

	void follow(String username);

	void unfollow(String username);

	CompletableFuture<AvatarResponse> setProfilePicture(String path);
	
	CompletableFuture<List<UserDto>> getSubscriptions(String username, int pageNo, int pageSize) throws InterruptedException;

	AvatarResponse resetProfilePicture();

	AvatarResponse getProfilePicture();

	CompletableFuture<List<UserDto>> getSubscribers(String username, int pageNo, int pageSize);

	boolean isAnonymous();

	User getAuthenticatedUser();
}
