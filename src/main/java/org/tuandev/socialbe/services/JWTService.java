package org.tuandev.socialbe.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JWTService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateToken(String token);
    boolean validateRefreshToken(String refreshToken);
    String getUsernameFromToken(String token);
    long getExpirationTime(String token);
}
