package com.example.demo.persistence.models;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name= "post")
public class Post implements Serializable {

	private static final long serialVersionUID = -8369347335051158514L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(length=1024)
	private String description;
	
	private String url;
	
	private Timestamp date;
	
	@ManyToOne(optional=false) //important @ManyToOne default fetch = EAGER	
	@JsonIgnoreProperties("posts")
	private User utilisateur;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // @OneToMany default fetch = LAZY
	@JsonManagedReference
	private List<Comment> comments = new ArrayList<>();
	
	@Transient
	private boolean isLiked;
	
	@Transient
	private Long countLike;

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
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
