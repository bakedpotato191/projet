package com.example.demo.persistence.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "utilisateur")
public class User implements Serializable {

	private static final long serialVersionUID = 5284328707299338410L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	@JsonIgnore
    private boolean enabled;

    //
    
	@JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "utilisateur_roles", 
    			joinColumns = @JoinColumn(
    					name = "user_id", 
    					referencedColumnName = "id"), 
    			inverseJoinColumns = @JoinColumn(
    					name = "role_id", 
    					referencedColumnName = "id"))
    private Collection<Role> roles;
    
	@JsonManagedReference
    @OneToMany(
    		mappedBy="utilisateur", 
    		fetch = FetchType.LAZY,
    		cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

	public User() { 
		super();
        this.enabled = false;	
	}
	
	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
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

	public void setPassword(final String password) {
		this.password = password;
	}
	
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
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

	@Override
    public int hashCode() {
        var prime = 31;
        var result = 1;
        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
    	if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var user = (User) obj;
        return getEmail().equals(user.getEmail());
    }   
}
