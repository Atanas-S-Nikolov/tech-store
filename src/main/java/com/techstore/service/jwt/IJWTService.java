package com.techstore.service.jwt;

import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.model.response.JWTResponse;

import javax.servlet.http.HttpServletRequest;

public interface IJWTService {
    JWTResponse refreshToken(HttpServletRequest request) throws InvalidJWTException;
}
