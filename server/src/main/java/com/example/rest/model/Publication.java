package com.example.rest.model;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name= "post")
public class Publication implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(length=1024)
	private String description;
	
	@Column
	private Timestamp date;

	@Column
	private String photo;
	
	@ManyToOne(optional=false) // @ManyToOne default fetch = EAGER
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties("posts")
	private User utilisateur;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonBackReference
	private List<Comment> comments = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<Favori> likes = new ArrayList<>();
	
	@Transient
	private boolean isLiked;
	
	@Transient
	private Long countLike;

	public Publication() {}

	public Publication(Long id, String description, Timestamp date, String photo, User utilisateur) {
		super();
		this.id = id;
		this.description = description;
		this.date = date;
		this.photo = photo;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public Long getCountLike() {
		return countLike;
	}

	public void setCountLike(Long countLike) {
		this.countLike = countLike;
	}

	@Override
	public int hashCode() {
		var prime = 31;
		var result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publication other = (Publication) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	private static final long serialVersionUID = 1L;
}
