package com.example.demo.mapstruct.dto;

import java.sql.Timestamp;

public class PostDto {
	
	private String url;
	private String description;
	private Timestamp date;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Timestamp getDate() {
		return date;
	}
	
	public void setDate(Timestamp date) {
		this.date = date;
	}		
}
