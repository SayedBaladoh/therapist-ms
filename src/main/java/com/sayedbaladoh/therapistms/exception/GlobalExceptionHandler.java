package com.sayedbaladoh.therapistms.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sayedbaladoh.therapistms.dto.ErrorItem;
import com.sayedbaladoh.therapistms.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler({ MismatchedInputException.class, NoSuchFieldException.class, NumberFormatException.class,
			JsonProcessingException.class, IllegalArgumentException.class })
	public ResponseEntity<ErrorItem> runtime(RuntimeException e) {
		log.info(e.getMessage());
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorItem> handle(InvalidFormatException e) {
		log.info(e.getMessage());
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getOriginalMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorItem> handle(ResourceNotFoundException e) {
		log.info(e.getMessage());
		ErrorItem error = new ErrorItem();
		error.setMessage(e.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
//	@ExceptionHandler(HttpClientErrorException.class)
//	public ResponseEntity<ErrorItem> handle(HttpClientErrorException e) {
//		log.info(e.getMessage());
//		ErrorItem error = new ErrorItem();
//		error.setMessage(e.getMessage());
//
//		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

//	@SuppressWarnings("rawtypes")
//	@ExceptionHandler(ConstraintViolationException.class)
//	public ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
//		ErrorResponse errors = new ErrorResponse();
//		for (ConstraintViolation violation : e.getConstraintViolations()) {
//			ErrorItem error = new ErrorItem();
//			error.setCode(violation.getMessageTemplate());
//			error.setMessage(violation.getMessage());
//			errors.addError(error);
//		}
//
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	}
//
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
		ErrorResponse errors = new ErrorResponse();
		e.getBindingResult().getAllErrors().forEach((err) -> {
			ErrorItem error = new ErrorItem();
			error.setCode(((FieldError) err).getField());
			error.setMessage(err.getDefaultMessage());
			errors.addError(error); 
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
//
//	@ExceptionHandler(ValidationViolationException.class)
//	public ResponseEntity<ErrorResponse> handle(ValidationViolationException e) {
//		ErrorResponse errors = new ErrorResponse();
//		for (ValidationViolation violation : e.getConstraintViolations()) {
//			ErrorItem error = new ErrorItem();
//			error.setCode(violation.getType().toString());
//			error.setMessage(violation.getMessage());
//			errors.addError(error);
//		}
//
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	}
}
