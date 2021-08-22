package com.example.demo.rest.dto;

import com.example.demo.validators.ValidUUID;

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
