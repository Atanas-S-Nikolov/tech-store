package com.techstore.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class FileUtils {
    public static String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID() + "-" + requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
