package com.example.demo.persistence.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="follower")
public class Follower implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	@ManyToOne
    @JoinColumn(name="from_user_fk")
    private User from;

    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private User to;
    
    public Follower() {};

    public Follower(User from, User to) {
        this.from = from;
        this.to = to;
    }

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}
	
	private static final long serialVersionUID = -7105547295149553264L;
}
