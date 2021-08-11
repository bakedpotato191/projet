package com.example.demo.persistence.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="follower", uniqueConstraints= @UniqueConstraint(columnNames={"utilisateur1_id", "utilisateur2_id"}))
public class Follower implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	@ManyToOne(optional=false)
	@JoinColumn
    private User utilisateur1;

    @ManyToOne(optional=false)
    @JoinColumn
    private User utilisateur2;
    
    public Follower() {
    	super();
    }

	public Follower(User utilisateur1, User utilisateur2) {
		super();
		this.utilisateur1 = utilisateur1;
		this.utilisateur2 = utilisateur2;
	}

	public User getUtilisateur1() {
		return utilisateur1;
	}

	public void setUtilisateur1(User utilisateur1) {
		this.utilisateur1 = utilisateur1;
	}

	public User getUtilisateur2() {
		return utilisateur2;
	}

	public void setUtilisateur2(User utilisateur2) {
		this.utilisateur2 = utilisateur2;
	}
	
	@Override
	public int hashCode() {
		var prime = 31;
		var result = 1;
		result = prime * result + ((utilisateur1 == null) ? 0 : utilisateur1.hashCode());
		result = prime * result + ((utilisateur2 == null) ? 0 : utilisateur2.hashCode());
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
		Follower other = (Follower) obj;
		if (utilisateur1 == null) {
			if (other.utilisateur1 != null)
				return false;
		} else if (!utilisateur1.equals(other.utilisateur1))
			return false;
		if (utilisateur2 == null) {
			if (other.utilisateur2 != null)
				return false;
		} else if (!utilisateur2.equals(other.utilisateur2))
			return false;
		return true;
	}

	private static final long serialVersionUID = -7105547295149553264L;
}
