package com.example.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.example.rest.model.User;

public class Event extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
    private final Locale locale;
    private final User user;

    public Event(final User user, final Locale locale) {
        super(user);
        this.user = user;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
