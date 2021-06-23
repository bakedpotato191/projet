package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.persistence.models.User;
import com.example.demo.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	
	@Autowired
	private UserService userService;

	@GetMapping(value= "/{username}")
    public User userPage(@PathVariable("username") String username) {
		return userService.getUserData(username);	
    }	
}
