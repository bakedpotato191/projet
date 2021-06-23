package com.example.demo.persistence.models;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name= "post")
public class Post implements Serializable {

	private static final long serialVersionUID = -8369347335051158514L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String description;
	
	private String url;
	
	private Timestamp date;
	
	@ManyToOne(optional=false)
	@JsonIgnoreProperties("posts")
	private User utilisateur;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JsonManagedReference
	private Set<Comment> comments = new HashSet<>();
	
	private boolean isLiked;

	public Post() {
		super();
	}

	public Post(Long id, String description, String url, User utilisateur) {
		super();
		this.id = id;
		this.description = description;
		this.url = url;
		this.utilisateur = utilisateur;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(User utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
}
