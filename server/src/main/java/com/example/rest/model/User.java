package com.example.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "utilisateur")
public class User implements Serializable, UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
    private Long id;

	@Column
	private String nom;

	@Column
    private String prenom;
    
    @Column(length=128)
    private String email;
    
    @Column(unique=true)
    private String username;

	@Column(length = 60)
	private String password;
    
    @Column(columnDefinition="varchar(255) default 'http://localhost:8081/api/user/profile_picture/default.jpg'")
    private String avatar;
    
    @Column(columnDefinition="boolean default false")
    private boolean has_avatar;
	
	@Column(columnDefinition="boolean default false")
    private boolean enabled;
	
	@Transient
	private Long postCount;
	
	@Transient
	private Long followerCount;
	
	@Transient
	private Long followingCount;
	
	@Transient
	private boolean followed;
	
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) // @ManyToMany default fetch = LAZY
    @JoinTable(name = "utilisateur_roles", 
    			joinColumns = @JoinColumn(
    					name = "role_id",
    					referencedColumnName = "id"),
    			inverseJoinColumns = @JoinColumn(
    					name = "user_id",
    					referencedColumnName = "id"))
    private Collection<Role> roles;
    
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL, orphanRemoval=true) // @OneToMany default fetch = LAZY
	@JsonIgnore
    private List<Publication> posts = new ArrayList<>();
       
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL)
    private Set<Favori> likes = new HashSet<>();
    
    @OneToOne(fetch=FetchType.LAZY, mappedBy="user", cascade = CascadeType.ALL, orphanRemoval=true)  // @OneToOne default fetch = EAGER	
    private VerificationToken token;
    
    @OneToMany(mappedBy="utilisateur2", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Follower> followers;
    
    @OneToMany(mappedBy="utilisateur1", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Follower> following;

    //
    
	public User() {}
	
	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public List<Publication> getPosts() {
		return posts;
	}

	public void setPosts(List<Publication> posts) {
		this.posts = posts;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Long getPostCount() {
		return postCount;
	}

	public void setPostCount(Long postCount) {
		this.postCount = postCount;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public Set<Favori> getLikes() {
		return likes;
	}

	public void setLikes(Set<Favori> likes) {
		this.likes = likes;
	}
	
	public List<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}

	public List<Follower> getFollowing() {
		return following;
	}

	public void setFollowing(List<Follower> following) {
		this.following = following;
	}
	
	public boolean isHas_avatar() {
		return has_avatar;
	}

	public void setHas_avatar(boolean has_avatar) {
		this.has_avatar = has_avatar;
	}

	public VerificationToken getToken() {
		return token;
	}

	public void setToken(VerificationToken token) {
		this.token = token;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
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

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@JsonIgnore
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public int hashCode() {
		var prime = 31;
		var result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	private static final long serialVersionUID = 1L;
}
