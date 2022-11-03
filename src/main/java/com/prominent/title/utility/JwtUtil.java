package com.prominent.title.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*This util contains method related to jwt token - generation, validation, etc. */

@Slf4j
@Service
public class JwtUtil {

    @Value("${SECRET}")
    private String secret;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * This method used to check is JWT token expired or not.
     *
     * @param token token
     * @return {@link Boolean}
     * @see Boolean
     */
    public Boolean isTokenExpired(String token) {
        log.info("Checking token expiration...");
        return extractExpiration(token).before(new Date());
    }

    /**
     * This method used to generate JWT token.
     *
     * @param username     username
     * @param milliseconds milliseconds
     * @return {@link String}
     * @see String
     */
    public String generateToken(String username, long milliseconds) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, milliseconds);
    }

    private String createToken(Map<String, Object> claims, String subject, long milliseconds) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + milliseconds))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * This method used to validate JWT token.
     *
     * @param token       token
     * @param userDetails userDetails
     * @return {@link Boolean}
     * @see Boolean
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
