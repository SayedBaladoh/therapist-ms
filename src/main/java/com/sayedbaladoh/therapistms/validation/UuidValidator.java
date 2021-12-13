package com.sayedbaladoh.therapistms.validation;

import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UuidValidator implements ConstraintValidator<Uuid, UUID> {
	
    private final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Override
    public void initialize(Uuid uuid) { }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext cxt) {
    	if (uuid!=null)
    		return uuid.toString().matches(UUID_PATTERN);
		return true;
    }
}
