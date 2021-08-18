package com.example.demo.rest.exceptions;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(Class<?> entityClass, String type, String id) {
		super(String.format("%s was not found for parameter %s = %s", entityClass.getSimpleName(), type, id));
	}
}