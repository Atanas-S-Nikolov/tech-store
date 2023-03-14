package com.techstore.controller;

import com.techstore.model.enums.UserRole;
import com.techstore.model.dto.AuthenticationDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.service.user.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.PAGE_PARAM;
import static com.techstore.constants.ApiConstants.SIZE_PARAM;
import static com.techstore.constants.ApiConstants.USERS_URL;
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

    @PostMapping(path = "/role/{role}",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(
            @PathVariable(value = "role") UserRole role,
            @RequestBody @Valid UserDto userDto)
    {
        return ResponseEntity.status(CREATED).body(service.createUserWithRole(userDto, role));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(value = PAGE_PARAM, defaultValue = "0") Integer page,
            @RequestParam(value = SIZE_PARAM, defaultValue = "10") Integer size) {
        return ResponseEntity.status(OK).body(service.getAllUsers(page, size));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(OK).body(service.updateUser(userDto));
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@RequestBody @Valid AuthenticationDto deletionDto) {
        service.deleteUser(deletionDto.getUsername(), deletionDto.getPassword());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
