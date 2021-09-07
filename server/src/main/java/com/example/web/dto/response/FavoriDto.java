package com.example.web.dto.response;

import java.sql.Timestamp;

public class FavoriDto {

	private Long id;
	
	private PublicationDto post;
	
	private Timestamp date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PublicationDto getPost() {
		return post;
	}

	public void setPost(PublicationDto post) {
		this.post = post;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
}
