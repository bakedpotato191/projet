package com.example.web.exception;

public class IncorrectFileExtensionException extends RuntimeException {

	private static final long serialVersionUID = 7240333150741709702L;
	
	public IncorrectFileExtensionException(String errorMessage) {
        super(errorMessage);
    }
	
	public IncorrectFileExtensionException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
