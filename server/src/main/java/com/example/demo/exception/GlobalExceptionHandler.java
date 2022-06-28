package com.example.demo.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// handling specific exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request) {
		ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(WrongInputException.class)
	public ResponseEntity<?> wrongInputExceptionHandling(WrongInputException exception, WebRequest request) {
		ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
	}
	// handling global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
		ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
