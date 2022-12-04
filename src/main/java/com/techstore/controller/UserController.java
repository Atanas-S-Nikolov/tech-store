package com.techstore.controller;

import com.techstore.model.response.JWTResponse;
import com.techstore.utils.converter.ModelConverter;
import com.techstore.model.dto.AuthenticationDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.response.UserResponse;
import com.techstore.service.user.AbstractUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.techstore.constants.ApiConstants.REFRESH_TOKEN_URL;
import static com.techstore.constants.ApiConstants.USERS_URL;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "user-controller")
@RequestMapping(value = USERS_URL)
public class UserController {
    private final AbstractUserService service;

    @Autowired
    public UserController(AbstractUserService service) {
        this.service = service;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createCustomer(@RequestBody UserDto userDto) {
        UserResponse userResponse = toResponse(service.createCustomer(toModel(userDto)));
        return ResponseEntity.status(CREATED).body(userResponse);
    }

    @PostMapping(path = "/admin", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createAdmin(@RequestBody UserDto userDto) {
        UserResponse userResponse = toResponse(service.createAdmin(toModel(userDto)));
        return ResponseEntity.status(CREATED).body(userResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserResponse>> getAllUsers() {
        List<UserResponse> users = service.getAllUsers().stream().map(ModelConverter::toResponse).collect(toList());
        return ResponseEntity.status(OK).body(users);
    }

    @GetMapping(path = REFRESH_TOKEN_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(service.refreshToken(request));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserDto userDto) {
        UserResponse userResponse = toResponse(service.updateUser(toModel(userDto)));
        return ResponseEntity.status(OK).body(userResponse);
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@RequestBody AuthenticationDto deletionDto) {
        service.deleteUser(deletionDto.getUsername(), deletionDto.getPassword());
        return ResponseEntity.status(OK).build();
    }
}
