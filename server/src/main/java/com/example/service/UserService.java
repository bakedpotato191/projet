package com.example.service;

import java.util.List;

import com.example.rest.model.Follower;
import com.example.rest.model.User;
import com.example.web.dto.response.AvatarResponse;

public interface UserService {

	User getUserFromSession();

	User getUserData(String username);

	void follow(String username);

	void unfollow(String username);

	AvatarResponse setProfilePicture(String path);
	
	List<Follower> getSubscriptions(String username, int pageNo, int pageSize);

	AvatarResponse resetProfilePicture();

	AvatarResponse getProfilePicture();

}
