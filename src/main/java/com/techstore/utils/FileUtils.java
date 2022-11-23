package com.techstore.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class FileUtils {
    public static File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = null;

        try {
            convertedFile = new File(requireNonNull(file.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedFile;
    }

    public static String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID() + "-" + requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
