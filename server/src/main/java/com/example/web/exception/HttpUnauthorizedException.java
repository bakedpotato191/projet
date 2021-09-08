package com.example.web.exception;

public class HttpUnauthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public HttpUnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
	
	public HttpUnauthorizedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
