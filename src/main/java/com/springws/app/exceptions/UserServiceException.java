package com.springws.app.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 2365357459378307912L;
	
	public UserServiceException(String message) {
		super(message);
	}
	
}
