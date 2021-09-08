package com.example.web.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
