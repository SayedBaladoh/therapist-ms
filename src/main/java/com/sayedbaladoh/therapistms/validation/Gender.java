package com.sayedbaladoh.therapistms.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Value;

@Documented
@Constraint(
		validatedBy = GenderValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Gender {
	
	String message() default "invalid gender! must be male or female";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}