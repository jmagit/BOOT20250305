package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
	private static final long serialVersionUID = 1L;
	public NotFoundException() {
		this(" ");
	}
	public NotFoundException(String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}
	public NotFoundException(String reason, Throwable cause) {
		super(HttpStatus.NOT_FOUND, reason, cause);
	}
}
