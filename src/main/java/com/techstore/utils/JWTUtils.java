package com.techstore.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techstore.exception.authentication.InvalidJWTException;

import java.util.List;
import java.util.Date;

import static com.techstore.constants.DateTimeConstants.FOUR_WEEKS;
import static com.techstore.constants.DateTimeConstants.ONE_HOUR;
import static com.techstore.constants.JWTConstants.ROLES_CLAIM;

import static java.lang.System.currentTimeMillis;

public class JWTUtils {

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
                .withExpiresAt(new Date(currentTimeMillis() + FOUR_WEEKS))
                .sign(algorithm);
    }

    public static Algorithm generateAlgorithmWithSecret(String secret) {
        return Algorithm.HMAC256(secret.getBytes());
    }
}
