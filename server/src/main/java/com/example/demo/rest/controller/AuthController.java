package com.example.demo.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.events.OnRegistrationCompleteEvent;
import com.example.demo.jwt.response.JwtResponse;
import com.example.demo.rest.dto.LoginDto;
import com.example.demo.rest.dto.PasswordRecoveryDto;
import com.example.demo.rest.dto.SignupDto;
import com.example.demo.rest.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody final LoginDto request){
		return userService.authenticateUser(request);
    }
    
	@PostMapping("/signup")
	public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid final SignupDto signUpRequest, final HttpServletRequest request) {
		var registered = userService.registerUser(signUpRequest);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/restore")
	public ResponseEntity<HttpStatus> restorePassword(@RequestBody final PasswordRecoveryDto dto) {
		System.out.println(dto.getEmail());
		userService.restorePassword(dto.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
