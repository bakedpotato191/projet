package com.example.demo.persistence.models;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "utilisateur")
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 5284328707299338410L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	@JsonIgnore
    private Long id;

	private String nom;

    private String prenom;
    
    @JsonIgnore
    @Column(length=128)
    private String email;
    
    @Column(unique=true)
    private String username;

    @JsonIgnore
	@Column(length = 60)
	private String password;
    
    private String avatar = "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg";
	
	@JsonIgnore
    private boolean enabled = false;
	
	@Transient
	private Long postCount;
	
	@Transient
	private Long followerCount;
	
	@Transient
	private boolean followed;
	
	@JsonIgnore
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) // @ManyToMany default fetch = LAZY
    @JoinTable(name = "utilisateur_roles", 
    			joinColumns = @JoinColumn(
    					name = "role_id",
    					referencedColumnName = "id"),
    			inverseJoinColumns = @JoinColumn(
    					name = "user_id",
    					referencedColumnName = "id"))
    private Collection<Role> roles;
    
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL) // @OneToMany default fetch = LAZY
	@JsonIgnoreProperties({"utilisateur", "comments"})
    private List<Post> posts = new ArrayList<>();
       
    @JsonIgnore
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy="utilisateur", cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();
    
    @JsonIgnore
    @OneToOne(fetch=FetchType.LAZY, mappedBy="user", cascade = CascadeType.ALL, orphanRemoval=true)  // @OneToOne default fetch = EAGER	
    private VerificationToken token;
    
    @JsonIgnore
    @OneToMany(mappedBy="utilisateur2", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Follower> followers;

    @JsonIgnore
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
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
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
	
	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
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
}
