package com.example.demo.exceptions;

public class HttpUnauthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = -8911825824585059250L;

	public HttpUnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
