package com.techstore.controller;

import com.techstore.service.mail.IMailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TestController {
    private final IMailSenderService service;

    @Autowired
    public TestController(IMailSenderService service) {
        this.service = service;
    }

    @PostMapping(path = "api/v1/mail/{email}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@PathVariable("email") String email) {
        //service.sendRegistrationMailConfirmation(email);
        return ResponseEntity.status(CREATED).body("Email sent!");
    }
}
