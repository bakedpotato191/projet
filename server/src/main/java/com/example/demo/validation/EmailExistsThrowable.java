package com.example.demo.validation;

public class EmailExistsThrowable extends Throwable {
	
	private static final long serialVersionUID = 1L;

	public EmailExistsThrowable(final String message) {
		super(message);
	}
}
