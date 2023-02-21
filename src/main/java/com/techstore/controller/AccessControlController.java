package com.techstore.controller;

import com.techstore.model.dto.UserDto;
import com.techstore.model.response.JWTResponse;
import com.techstore.model.response.UserResponse;
import com.techstore.service.access.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.ACCESS_CONTROL_URL;
import static com.techstore.constants.ApiConstants.CREATE_CUSTOMER_URL;
import static com.techstore.constants.ApiConstants.REFRESH_TOKEN_URL;
import static com.techstore.constants.ApiConstants.REGISTER_URL;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController(value = "access-control-controller")
@RequestMapping(value = ACCESS_CONTROL_URL)
public class AccessControlController {
    private final AccessControlService service;

    @Autowired
    public AccessControlController(AccessControlService service) {
        this.service = service;
    }

    @PostMapping(path = REGISTER_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserDto userDto) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(CREATE_CUSTOMER_URL).toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);
        return new RestTemplate().postForEntity(url, requestEntity, UserResponse.class);
    }

    @GetMapping(path = REFRESH_TOKEN_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.status(OK).body(service.refreshToken(request));
    }
}
