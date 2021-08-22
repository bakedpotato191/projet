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

import com.example.demo.events.OnPasswordResetRequestedEvent;
import com.example.demo.events.OnRegistrationCompleteEvent;
import com.example.demo.jwt.response.JwtResponse;
import com.example.demo.rest.dto.LoginDto;
import com.example.demo.rest.dto.NewPasswordDto;
import com.example.demo.rest.dto.PasswordResetDto;
import com.example.demo.rest.dto.SignupDto;
import com.example.demo.rest.dto.TokenDto;
import com.example.demo.rest.services.AuthService;

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
	public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid final SignupDto signUpRequest, final HttpServletRequest request) {
		var registered = authService.registerUser(signUpRequest);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale()));
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/restore")
	public ResponseEntity<HttpStatus> restorePassword(@RequestBody @Valid final PasswordResetDto dto, final HttpServletRequest request) {
		var user = authService.findUserByEmail(dto.getEmail());
		if (user.isPresent()) {
			eventPublisher.publishEvent(new OnPasswordResetRequestedEvent(user.get(), request.getLocale()));
		}
		return new ResponseEntity<>(HttpStatus.OK);
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
