package com.example.web.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 8399279269270718202L;

	public UsernameAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
	
	public UsernameAlreadyExistsException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
