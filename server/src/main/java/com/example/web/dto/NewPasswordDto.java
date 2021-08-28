package com.example.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.validation.PasswordMatches;
import com.example.validation.ValidUUID;

@PasswordMatches
public class NewPasswordDto {
	
	private String password;

	@ValidUUID
    private String token;

    @NotNull
    @Size(min=8, max=32)
    private String passwordRepeat;

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
