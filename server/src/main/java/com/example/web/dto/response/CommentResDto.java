package com.example.web.dto.response;

import java.sql.Timestamp;

public class CommentResDto {
	
	private Long id;
	
	private String text;
	
	private Timestamp date;
	
	private UserDto utilisateur;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public UserDto getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(UserDto utilisateur) {
		this.utilisateur = utilisateur;
	}
}
