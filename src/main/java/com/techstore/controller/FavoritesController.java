package com.techstore.controller;

import com.techstore.model.dto.FavoritesDto;
import com.techstore.model.dto.UsernameDto;
import com.techstore.model.response.FavoritesResponse;
import com.techstore.service.favorites.IFavoritesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.techstore.constants.ApiConstants.ADD_FAVORITES_URL;
import static com.techstore.constants.ApiConstants.FAVORITES_URL;
import static com.techstore.constants.ApiConstants.REMOVE_FAVORITES_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "favorites-controller")
@RequestMapping(value = FAVORITES_URL)
public class FavoritesController {
    private final IFavoritesService service;

    @Autowired
    public FavoritesController(IFavoritesService service) {
        this.service = service;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoritesResponse> getFavorites(@RequestBody UsernameDto usernameDto) {
        return ResponseEntity.status(OK).body(service.getFavorites(usernameDto.getUsername()));
    }

    @PutMapping(path = ADD_FAVORITES_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoritesResponse> addFavorite(@RequestBody FavoritesDto dto) {
        return ResponseEntity.status(OK).body(service.addFavorite(dto));
    }

    @PutMapping(path = REMOVE_FAVORITES_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoritesResponse> removeFavorite(@RequestBody FavoritesDto dto) {
        return ResponseEntity.status(OK).body(service.removeFavorite(dto));
    }

    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFavorites(@RequestBody UsernameDto usernameDto) {
        service.deleteFavorites(usernameDto.getUsername());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
