package com.example.event;

import java.util.Locale;

import com.example.rest.model.User;

public class OnRegistrationCompleteEvent extends Event {

	private static final long serialVersionUID = 1L;

	public OnRegistrationCompleteEvent(User user, Locale locale) {
		super(user, locale);
	}

}
