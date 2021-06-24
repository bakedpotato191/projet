package com.example.demo.mapstruct.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordResetDto {
	
	private String oldPassword;

    private  String token;

    @NotNull
    @Size(min=8, max=32)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
