package com.techstore.controller;

import com.techstore.model.response.JWTResponse;
import com.techstore.service.access.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.techstore.constants.ApiConstants.ACCESS_CONTROL_URL;
import static com.techstore.constants.ApiConstants.REFRESH_TOKEN_URL;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "access-control-controller")
@RequestMapping(value = ACCESS_CONTROL_URL)
public class AccessControlController {
    private final AccessControlService service;

    @Autowired
    public AccessControlController(AccessControlService service) {
        this.service = service;
    }

    @GetMapping(path = REFRESH_TOKEN_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(service.refreshToken(request));
    }
}
