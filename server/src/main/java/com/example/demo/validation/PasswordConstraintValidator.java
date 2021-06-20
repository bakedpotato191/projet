package com.example.demo.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	private static final String PASSWORD_PATTERN="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,32}$";
	private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);
	
    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
    	return (validatePassword(password));
    	
    }
    
    private boolean validatePassword(final String password) {

        if (password == null) {
            return false;
        }

        var matcher = PATTERN.matcher(password);
        return matcher.matches();
    }
}