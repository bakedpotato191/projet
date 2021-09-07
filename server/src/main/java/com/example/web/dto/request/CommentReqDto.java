package com.example.web.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentReqDto {
	
	@NotNull
	private Long post_id;
	
	@NotNull
	@Size(min=2, max=128)
	private String comment;

	public Long getPost_id() {
		return post_id;
	}

	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
