package com.springws.app.security;

import com.springws.app.SpringApplicationContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 86400000;
	public static final long EXPIRATION_TIME_PASSWORD_RESET = 3600000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String VERIFICATION_EMAIL_URL = "/users/verification-email";
	public static final String SIGN_IN_URL = "/users/login";
	public static final String RESET_PASSWORD_REQUEST = "/users/password-reset-request";
	public static final String PASSWORD_RESET = "/users/password-reset";
	public static final String TOKEN_SECRET = getTokenSecret();
	
	private static String getTokenSecret() {
		AppProperties props = (AppProperties) SpringApplicationContext.getBean("appProperties");
		return props.getTokenSecret();
	}

}
