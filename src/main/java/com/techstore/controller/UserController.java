package com.techstore.controller;

import com.techstore.model.dto.EmailDto;
import com.techstore.model.dto.UpdateUserDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.dto.UsernameDto;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.service.user.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController(value = "user-controller")
@RequestMapping(value = USERS_URL)
public class UserController {
    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(CREATED).body(service.createUser(userDto));
    }

    @PostMapping(path = GET_URL,consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser(@RequestBody @Valid UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.getUser(usernameDto.getUsername()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") Integer page,
            @RequestParam(value = SIZE_PARAM, defaultValue = "10") Integer size) {
        return ResponseEntity.status(OK).body(service.getAllUsers(page, size));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserDto userDto) {
        return ResponseEntity.status(OK).body(service.updateUser(userDto));
    }

    @PutMapping(path = FORGOT_PASSWORD_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> forgotPassword(@RequestBody EmailDto emailDto) {
        return ResponseEntity.status(OK).body(service.forgotPassword(emailDto));
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@RequestBody @Valid UsernameDto usernameDto) {
        service.deleteUser(usernameDto.getUsername());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
