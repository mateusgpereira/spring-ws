package com.springws.app.security;

import com.springws.app.SpringApplicationContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 86400000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String TOKEN_SECRET = getTokenSecret();
	
	private static String getTokenSecret() {
		AppProperties props = (AppProperties) SpringApplicationContext.getBean("appProperties");
		return props.getTokenSecret();
	}

}
