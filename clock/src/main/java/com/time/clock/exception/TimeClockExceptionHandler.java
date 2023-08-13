package com.time.clock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TimeClockExceptionHandler {

	@ExceptionHandler(value = { TimeClockException.class })
	public ResponseEntity<Object> handleAPIRequestException(TimeClockException vechiclesException) {
		HttpStatus httpStatus = vechiclesException.getStatusCode();
		TimeClockException exception = new TimeClockException(httpStatus, vechiclesException.getMessage());
		return ResponseEntity.status(httpStatus).body(exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleConstraintViolationExceptions(MethodArgumentNotValidException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input parameters:");
	}

}
