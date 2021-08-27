package com.example.demo.rest.response;

import org.springframework.http.HttpStatus;

public class AvatarResponse {
	
	private boolean has_avatar;
	
	private String avatar;
	
	private HttpStatus status;

	public AvatarResponse(boolean has_avatar, String avatar, HttpStatus status) {
		super();
		this.has_avatar = has_avatar;
		this.avatar = avatar;
		this.status = status;
	}

	public boolean getHas_avatar() {
		return has_avatar;
	}

	public void setHas_avatar(boolean has_avatar) {
		this.has_avatar = has_avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getAvatar() {
		return avatar;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
