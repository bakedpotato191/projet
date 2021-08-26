package com.example.demo.rest.services;

import java.util.List;

import com.example.demo.rest.models.Follower;
import com.example.demo.rest.models.User;

public interface UserService {

	User getUserFromSession();

	User getUserData(String username);

	void follow(String username);

	void unfollow(String username);

	String setProfilePicture(String path);
	
	List<Follower> getSubscriptions(String username, int pageNo, int pageSize);

	void resetProfilePicture();

	String getProfilePicture();

}
