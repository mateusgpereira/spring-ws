package com.springws.app.shared;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final SecureRandom random = new SecureRandom();

	public String generateUserId(int length) {
		return this.generateRandomString(length);
	}

	private String generateRandomString(int length) {

		int leftLimit = 48; // number '0'
		int rightLimit = 122; // letter 'z'

		String randomStr = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return randomStr;
	}
}
