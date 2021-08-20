package com.example.demo.rest.dto;

import com.example.demo.validators.ValidEmail;

public class PasswordRecoveryDto {

	@ValidEmail
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
