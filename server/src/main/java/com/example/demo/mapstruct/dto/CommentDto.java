package com.example.demo.mapstruct.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDto {
	
	@NotNull
	private Long id;
	
	@NotNull
	@Size(min=2, max=128)
	private String text;
	

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
}
