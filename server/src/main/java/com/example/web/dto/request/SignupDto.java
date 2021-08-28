	package com.example.web.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.validation.ValidEmail;

public class SignupDto {
	
	@NotNull
	@Size(min = 2)
	private String nom;
	
	@NotNull
    @Size(min = 2)
	private String prenom;
	
	@NotNull
    @Size(min = 3, max=20)
	private String username;

	@ValidEmail
    private String email;

    @NotNull
    @Size(min=8, max=32)
    private String password;

    private Integer role;
    
    public String getEmail() {
        return email;
    }
 
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
        return password;
    }
 
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public Integer getRole() {
      return this.role;
    }
    
    public void setRole(final Integer role) {
      this.role = role;
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
}
