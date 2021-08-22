package com.example.demo.rest.services;

import com.example.demo.rest.models.User;

public interface UserService {

	User getUserFromSession();

	User getUserData(String username);

	void follow(String username);

	void unfollow(String username);
}
