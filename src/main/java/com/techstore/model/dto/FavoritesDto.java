package com.techstore.model.dto;

import com.techstore.validation.favorites.ValidFavorites;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@ValidFavorites
public class FavoritesDto {
    @NotBlank(message = "Username must not be blank")
    @Getter
    private final String username;

    @Getter
    private final Set<String> productNames;
}
