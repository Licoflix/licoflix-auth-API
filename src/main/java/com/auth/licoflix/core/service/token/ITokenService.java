package com.auth.licoflix.core.service.token;

import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenService {
    String generateToken(UserDetails userDetails);

    String extractUserEmail(String token);

    boolean isTokenValid(String token);

    Long extractUserId(String token);
}