package com.example.event;

import java.util.Locale;

import com.example.rest.model.User;

public class OnPasswordResetRequestedEvent extends Event {

	private static final long serialVersionUID = 1L;

	public OnPasswordResetRequestedEvent(User user, Locale locale) {
		super(user, locale);
	}

}
