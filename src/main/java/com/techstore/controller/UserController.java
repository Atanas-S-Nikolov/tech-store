package com.techstore.controller;

import com.techstore.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "user-controller")
@RequestMapping(value = "api/v1/user")
public class UserController {
    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    // TODO: register, login, logout -> POST -> ROLE_ADMIN && ROLE_CUSTOMER
    // TODO: getAll, update, delete -> GET, PUT, DELETE -> ROLE_ADMIN
}
