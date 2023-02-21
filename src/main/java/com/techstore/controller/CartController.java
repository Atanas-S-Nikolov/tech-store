package com.techstore.controller;

import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.UsernameDto;
import com.techstore.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.ADD_URL;
import static com.techstore.constants.ApiConstants.PURCHASE_URL;
import static com.techstore.constants.ApiConstants.CARTS_URL;
import static com.techstore.constants.ApiConstants.CLEAR_CART_URL;
import static com.techstore.constants.ApiConstants.REMOVE_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController(value = "cart-controller")
@RequestMapping(value = CARTS_URL)
public class CartController {
    private final ICartService service;

    @Autowired
    public CartController(ICartService service) {
        this.service = service;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCart(@RequestBody @Valid UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.getCart(usernameDto.getUsername()));
    }

    @PutMapping(path = ADD_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> addProductToCart(@RequestBody @Valid CartDto dto) {
        return ResponseEntity.status(OK).body(service.addProductToCart(dto));
    }

    @PutMapping(path = REMOVE_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> removeProductToCart(@RequestBody @Valid CartDto dto) {
        return ResponseEntity.status(OK).body(service.removeProductFromCart(dto));
    }

    @PutMapping(path = PURCHASE_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> purchase(@RequestBody @Valid UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.doPurchase(usernameDto.getUsername()));
    }

    @PutMapping(path = CLEAR_CART_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> clearCart(@RequestBody @Valid UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.clearCart(usernameDto.getUsername()));
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCart(@RequestBody @Valid UsernameDto usernameDto) {
        service.deleteCart(usernameDto.getUsername());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
