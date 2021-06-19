package com.example.demo.mapstruct.dto;

import javax.validation.constraints.NotNull;

import com.example.demo.validation.ValidEmail;
import com.example.demo.validation.ValidPassword;

public class LoginDto {
	
	@ValidEmail
	@NotNull
	private String email;

    @ValidPassword
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
