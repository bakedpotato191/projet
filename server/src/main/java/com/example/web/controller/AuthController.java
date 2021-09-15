package com.example.web.controller;

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

import com.example.event.OnPasswordResetRequestedEvent;
import com.example.event.OnRegistrationCompleteEvent;
import com.example.jwt.response.JwtResponse;
import com.example.service.AuthService;
import com.example.web.dto.request.LoginDto;
import com.example.web.dto.request.NewPasswordDto;
import com.example.web.dto.request.PasswordResetDto;
import com.example.web.dto.request.SignupDto;
import com.example.web.dto.request.TokenDto;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody @Valid final LoginDto request){
		return authService.authenticateUser(request);
    }
    
	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody @Valid final SignupDto signUpRequest, final HttpServletRequest request) {
		var registered = authService.registerUser(signUpRequest);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale()));
        return new ResponseEntity<>("{\"success\": true}", HttpStatus.OK);
	}
	
	@PostMapping("/restore")
	public ResponseEntity<String> restorePassword(@RequestBody @Valid final PasswordResetDto dto, final HttpServletRequest request) {
		var user = authService.findUserByEmail(dto.getEmail());
		if (user.isPresent()) {
			eventPublisher.publishEvent(new OnPasswordResetRequestedEvent(user.get(), request.getLocale()));
		}
		return new ResponseEntity<>("{\"success\": true}", HttpStatus.OK);
	}
	
	@PostMapping("/verify_token")
	public ResponseEntity<HttpStatus> verify(@RequestBody @Valid final TokenDto token) {
		authService.validatePasswordResetToken(token.getToken());
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/reset_password")
	public ResponseEntity<HttpStatus> reset(@RequestBody @Valid final NewPasswordDto dto) {
		authService.updatePassword(dto);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
