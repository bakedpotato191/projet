package com.example.web.dto.response;

import java.util.Collection;


import com.example.rest.model.Role;

public class UserDto {

	private String nom;

    private String prenom;

    private String username;

    private String avatar;
    
    private boolean has_avatar;
    
    private boolean enabled;
    
	private Long postCount;
	
	private Long followerCount;
	
	private Long followingCount;
	
	private boolean followed;
	
	private Collection<Role> roles;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isHas_avatar() {
		return has_avatar;
	}

	public void setHas_avatar(boolean has_avatar) {
		this.has_avatar = has_avatar;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getPostCount() {
		return postCount;
	}

	public void setPostCount(Long postCount) {
		this.postCount = postCount;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public Long getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(Long followingCount) {
		this.followingCount = followingCount;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
}
