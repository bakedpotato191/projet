package com.example.web.dto.response;

import java.sql.Timestamp;
import java.util.List;

public class PublicationDto {
	
    private Long id;
    
	private String description;
	
	private Timestamp date;

	private String photo;
	
	private UserDto utilisateur;
	
	private boolean isLiked;
	
	private int countLike;
	
	private int commentsCount;
	
	private boolean isAuthor;
	
	private List<CommentResDto> comments;
	
	public List<CommentResDto> getComments() {
		return comments;
	}

	public void setComments(List<CommentResDto> comments) {
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public UserDto getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(UserDto utilisateur) {
		this.utilisateur = utilisateur;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public int getCountLike() {
		return countLike;
	}

	public void setCountLike(int count) {
		this.countLike = count;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public boolean isAuthor() {
		return isAuthor;
	}

	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}
}
