package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@GetMapping(value= "/getuser/{username}")
    public User getUser(@PathVariable("username") String username) {
		return userService.getUserData(username);	
    }
	
	@PostMapping(value= "/subscribe/{username}")
    public ResponseEntity<HttpStatus> subscribe(@RequestBody @Valid final String username) {
		userService.follow(username);
		return new ResponseEntity<>(HttpStatus.OK);
		
    }
	
	@PostMapping(value= "/unsubscribe/{username}")
    public ResponseEntity<HttpStatus> unsubscribe(@RequestBody @Valid final String username) {
		userService.unfollow(username);
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
