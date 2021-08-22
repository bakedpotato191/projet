package com.example.demo.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {
	
    private static final String UUID_PATTERN = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";
    private static final Pattern PATTERN = Pattern.compile(UUID_PATTERN);

    @Override
    public boolean isValid(final String uuid, final ConstraintValidatorContext context) {
        return (validateUUID(uuid));
    }

    private boolean validateUUID(final String uuid) {
    	if (uuid == null) {
    		return false;
    	}
        return PATTERN.matcher(uuid).matches();
    }
}
