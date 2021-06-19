package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserService;
import com.example.demo.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
	private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody final LoginDto request){
    	
    	if (userService.checkIfUserEnabled(request.getEmail())) {
    	
    	//attemps to authenticate and returns populated authentication object
    	var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	//generates jwt token from authentication generated token
    	String jwt = jwtUtils.generateJwtToken(authentication);
    	
    	var userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
				 userDetails.getId(),  
				 userDetails.getEmail(), 
				 roles));
    	}
    	else {
    		return new ResponseEntity<>("No user found with email", HttpStatus.UNAUTHORIZED);
    	}
    }
    
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody final SignupDto signUpRequest, final HttpServletRequest request) {
		var registered = userService.registerNewUserAccount(signUpRequest);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return ResponseEntity.ok(new MessageResponse("Confirmation link was sent to your email."));
	}
	
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
