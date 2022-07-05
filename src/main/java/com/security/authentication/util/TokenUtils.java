package com.security.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

public class TokenUtils {
    public static final long TOKEN_VALIDITY = 1000L * 3600L;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ENCODED_SECRET_KEY = Base64.getEncoder()
            .encodeToString("^&$%^$^*#&%@".getBytes());

    private TokenUtils() {}

    public static String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public static String generateToken(String username) {
        Objects.requireNonNull(username, () -> {
            throw new BadCredentialsException("Invalid username! Cannot create authentication.");
        });
        Date issuedAt = new Date();
        Date expiry = new Date(issuedAt.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, ENCODED_SECRET_KEY)
                .compact();
    }

    public static boolean validate(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public static String extractToken(HttpServletRequest request) {
        final String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.nonNull(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            return tokenHeader.substring(7);
        }
        return null;
    }

    private static Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private static boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(ENCODED_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getClaims(token));
    }
}
