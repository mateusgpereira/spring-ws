package com.springws.app.shared;

import java.security.SecureRandom;
import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Component;

import com.springws.app.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

	private final SecureRandom random = new SecureRandom();

	public String generateUserId(int length) {
		return this.generateRandomString(length);
	}
	
	public String generateAddressId(int length) {
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
	
	public static boolean hasTokenExpired(String token) {

		try {
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token).getBody();

			Date tokenExpiration = claims.getExpiration();
			Date today = new Date();

			return tokenExpiration.before(today);
		} catch (ExpiredJwtException e) {
			return true;
		}

	}
	
	public String generateEmailVerificationToken(String userId) {
		return generateToken(userId, SecurityConstants.EXPIRATION_TIME);
	}

	public String generatePasswordResetToken(String userId) {
		return generateToken(userId, SecurityConstants.EXPIRATION_TIME_PASSWORD_RESET);
	}

	private String generateToken(String userId, long expirationTimeInMilis) {
		return Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMilis))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
				.compact();
	}
}
