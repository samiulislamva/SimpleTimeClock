package com.time.clock.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeClockException extends Exception {

	private static final long serialVersionUID = 1L;

	private HttpStatus statusCode;

	public TimeClockException() {
		super();
	}

	public TimeClockException(HttpStatus code, String message) {
		super(message);
		this.statusCode = code;
	}

}
