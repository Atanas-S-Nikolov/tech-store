package com.techstore.controller;

import com.techstore.model.dto.UserDto;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.JWTResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.service.access.AccessControlService;
import com.techstore.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.*;
import static com.techstore.model.enums.UserRole.ROLE_CUSTOMER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController(value = "access-control-controller")
@RequestMapping(value = ACCESS_CONTROL_URL)
public class AccessControlController {
    private final AccessControlService service;
    private final IUserService userService;

    @Autowired
    public AccessControlController(AccessControlService service, IUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping(path = REGISTER_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> register(@RequestBody UserDto userDto) {
        userDto.setRole(ROLE_CUSTOMER.getValue());
        return ResponseEntity.status(CREATED).body(userService.createUser(userDto));
    }

    @PostMapping(path = REFRESH_TOKEN_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(service.refreshToken(request));
    }

    @GetMapping(path = CONFIRM_REGISTER_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> confirmRegistration(@RequestParam(value = TOKEN_PARAM) String token) {
        return ResponseEntity.status(OK).body(service.confirmRegistration(token));
    }
}
