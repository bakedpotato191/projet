package com.example.demo.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	
    private static final String EMAIL_PATTERN = "^(.+)@(.+)$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
		System.out.println(email);
        return (validateEmail(email));
    }

    private boolean validateEmail(final String email) {
    	if (email == null) {
    		return false;
    	}
        return PATTERN.matcher(email).matches();
    }
}
