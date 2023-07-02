package com.techstore.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class ImageUploadUtils {
    public final static String FORWARD_SLASH =  "/";
    public final static String URL_ENCODED_FORWARD_SLASH =  "%2F";

    public static String formatUrl(String str) {
        return str.replace(FORWARD_SLASH, URL_ENCODED_FORWARD_SLASH);
    }

    public static String formatDeleteUrl(String str) {
        return str.replace(URL_ENCODED_FORWARD_SLASH, FORWARD_SLASH);
    }

    public static String generateFilePath(MultipartFile imageFile, String directoryName) {
        return (directoryName + FORWARD_SLASH + generateFileName(imageFile)).replace("?", "");
    }

    private static String generateFileName(MultipartFile imageFile) {
        return UUID.randomUUID() + "-" + requireNonNull(imageFile.getOriginalFilename()).replace(" ", "_");
    }
}
