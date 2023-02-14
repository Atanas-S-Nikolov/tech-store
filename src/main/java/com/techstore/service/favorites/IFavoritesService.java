package com.techstore.service.favorites;

import com.techstore.model.dto.FavoritesDto;
import com.techstore.model.entity.FavoritesEntity;
import com.techstore.model.response.FavoritesResponse;

public interface IFavoritesService {
    FavoritesEntity createDefaultFavorites();

    FavoritesResponse getFavorites(String username);

    FavoritesResponse addFavorite(FavoritesDto favoritesDto);

    FavoritesResponse removeFavorite(FavoritesDto favoritesDto);

    void deleteFavorites(String username);
}
