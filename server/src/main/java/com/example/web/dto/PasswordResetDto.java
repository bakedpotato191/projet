package com.example.web.dto;

import com.example.validation.ValidEmail;

public class PasswordResetDto {

	@ValidEmail
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
