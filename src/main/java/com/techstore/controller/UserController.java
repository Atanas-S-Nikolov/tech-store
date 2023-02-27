package com.techstore.controller;

import com.techstore.model.enums.UserRole;
import com.techstore.model.dto.AuthenticationDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.response.UserResponse;
import com.techstore.service.user.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

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
    public ResponseEntity<Collection<UserResponse>> getAllUsers() {
        return ResponseEntity.status(OK).body(service.getAllUsers());
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
