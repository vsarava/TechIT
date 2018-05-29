package techit.security;

import java.security.Key;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import techit.model.User;

public class SecurityUtils {
	private final static String KEY = "SECRET";
	private final static BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();
	
	public static String encodePassword(String rawPassword) {
		return BCRYPT.encode(rawPassword);
	}
	
	public static boolean checkPassword(String rawPassword, String encodedPassword) {
		return BCRYPT.matches(rawPassword, encodedPassword);
	}
	
	public static String createJwtToken( User user ) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("user", user)
				.signWith(SignatureAlgorithm.HS512, KEY)
				.compact();
	}
	
	public static Object getUser(String token) {
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("user");
	}
}
