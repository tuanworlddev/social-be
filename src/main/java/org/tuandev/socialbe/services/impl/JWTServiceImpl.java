package org.tuandev.socialbe.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tuandev.socialbe.services.JWTService;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.access-secret}")
    private String accessSecret;
    @Value("${jwt.refresh-secret}")
    private String refreshSecret;
    @Value("${jwt.access-validity}")
    private long accessTokenValidity;
    @Value("${jwt.refresh-validity}")
    private long refreshTokenValidity;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Key key = Keys.hmacShaKeyFor(accessSecret.getBytes());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        Key key = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        return !tokenExpired(token);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        return !refreshTokenExpired(refreshToken) && !Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + refreshToken));
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(accessSecret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject();
    }

    @Override
    public long getExpirationTime(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(refreshSecret.getBytes())).build().parseSignedClaims(token).getPayload().getExpiration().getTime();
    }

    private boolean tokenExpired(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(accessSecret.getBytes())).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    private boolean refreshTokenExpired(String refreshToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(refreshSecret.getBytes())).build().parseSignedClaims(refreshToken).getPayload().getExpiration().before(new Date());
    }
}
