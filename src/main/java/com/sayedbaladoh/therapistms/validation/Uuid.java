package com.sayedbaladoh.therapistms.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface Uuid {
	
	String message() default "invalid UUID";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
