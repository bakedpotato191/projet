package com.example.web.exception;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(Class<?> entityClass, String type, String value) {
		super(String.format("%s was not found for parameter %s = %s", entityClass.getSimpleName(), type, value));
	}
}