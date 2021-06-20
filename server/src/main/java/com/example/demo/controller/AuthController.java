package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mapstruct.dto.LoginDto;
import com.example.demo.mapstruct.dto.SignupDto;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.registration.OnRegistrationCompleteEvent;
import com.example.demo.security.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody @Valid final LoginDto request){

		return userService.authenticateUser(request);
    }
    
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid final SignupDto signUpRequest, final HttpServletRequest request) {
		var registered = userService.registerNewUserAccount(signUpRequest);
		
		if (registered != null) {
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		}
		
        return ResponseEntity.ok(new MessageResponse("Confirmation link was sent to your email."));
	}
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
