package com.example.demo.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -2713393064861894266L;

	public EmailAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
