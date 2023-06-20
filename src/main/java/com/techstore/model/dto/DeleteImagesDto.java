package com.techstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DeleteImagesDto {
    private final Collection<String> urls;
}
