package com.example.demo.persistence.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="postlikes")
public class Like implements Serializable {
	
	private static final long serialVersionUID = -4538179406393643986L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties("posts")
	private User utilisateur;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"utilisateur", "comments"})
	private Post post;
	
	public Like() {}

	public Like(Long id, User utilisateur, Post post) {
		super();
		this.id = id;
		this.utilisateur = utilisateur;
		this.post = post;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(User utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
