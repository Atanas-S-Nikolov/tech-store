package com.techstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ImageDto {
    private final MultipartFile file;
    private final boolean isMain;
}
