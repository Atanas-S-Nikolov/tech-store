package com.techstore.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techstore.exception.authentication.InvalidJWTException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Date;

import static com.techstore.constants.JWTConstants.ROLES_CLAIM;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;

public class JWTUtils {
    private static final long ONE_MINUTE = 1000L * 60L;
    private static final long ONE_HOUR = ONE_MINUTE *  60L;
    private static final long ONE_DAY = ONE_HOUR * 24L;
    private static final long THIRTY_DAYS = ONE_DAY * 30L;
    
    public static DecodedJWT verifyToken(String token, Algorithm algorithm) {
        DecodedJWT decodedJWT;
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(token);
        } catch (AlgorithmMismatchException | SignatureVerificationException | MissingClaimException | IncorrectClaimException e) {
            throw new InvalidJWTException("Invalid JWT authentication", e);
        }
        return decodedJWT;
    }

    public static String generateAccessToken(String username, List<String> roles, Algorithm algorithm) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(currentTimeMillis() + ONE_HOUR))
                .withClaim(ROLES_CLAIM, roles)
                .sign(algorithm);
    }

    public static String generateRefreshToken(String username, Algorithm algorithm) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(currentTimeMillis() + THIRTY_DAYS))
                .sign(algorithm);
    }

    public static Algorithm generateAlgorithmWithSecret(String secret) {
        return Algorithm.HMAC256(secret.getBytes());
    }

    public static List<String> convertAuthoritiesToStrings(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList());
    }

    public static List<GrantedAuthority> convertStringsToAuthorities(List<String> authoritiesStrings) {
        return authoritiesStrings.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }
}
