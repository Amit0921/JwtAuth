package com.test.jwtDemo.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.test.jwtDemo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret.key}")
	private String SECRET_KEY;

	@Value("${jwt.secret.validity}")
	private long validity_duration;
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		User user = (User) userDetails;
		extraClaims.put("id", user.getId());
		extraClaims.put("role", user.getRole());
		return Jwts.builder()
				.claims(extraClaims).subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + validity_duration))
				.signWith(getSignInKey())
				.compact();
	}


	public boolean isTokenValid(String jwt, UserDetails userDetails) {
		final String extractedUser = extractUsername(jwt);
		return (userDetails.getUsername().equals(extractedUser) && !isTokenExpired(jwt));
	}

	private boolean isTokenExpired(String jwt) {
		return extractExpiration(jwt).before(new Date(System.currentTimeMillis()));
	}

	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}
	
	public Date extractExpiration(String jwt) {
		return extractClaim(jwt, Claims::getExpiration);
	}
	
	public Long extractUserId(String jwt) {
		Claims claims = extractAllClaims(jwt);
		Map<String,Object> map = new HashMap<>(claims);
		return ((Number) map.get("id")).longValue();
	}
	
	private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(jwt);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String jwt) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwt).getPayload();
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
