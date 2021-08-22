package com.example.demo.events;

import java.util.Locale;

import com.example.demo.rest.models.User;

public class OnPasswordResetRequestedEvent extends Event {

	private static final long serialVersionUID = 1L;

	public OnPasswordResetRequestedEvent(User user, Locale locale) {
		super(user, locale);
	}

}
