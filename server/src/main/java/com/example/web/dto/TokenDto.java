package com.example.web.dto;

import com.example.validation.ValidUUID;

public class TokenDto {

	@ValidUUID
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
