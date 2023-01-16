package com.techstore.controller;

import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.UsernameDto;
import com.techstore.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.techstore.constants.ApiConstants.CARTS_URL;
import static com.techstore.constants.ApiConstants.CLEAR_CART_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "cart-controller")
@RequestMapping(value = CARTS_URL)
public class CartController {
    private final ICartService service;

    @Autowired
    public CartController(ICartService service) {
        this.service = service;
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCart(@RequestBody UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.getCart(usernameDto.getUsername()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> updateCart(@RequestBody CartDto dto) {
        return ResponseEntity.status(OK).body(service.updateCart(dto));
    }

    @PutMapping(path = CLEAR_CART_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> clearCart(@RequestBody UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.clearCart(usernameDto.getUsername()));
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCart(@RequestBody UsernameDto usernameDto) {
        service.deleteCart(usernameDto.getUsername());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
