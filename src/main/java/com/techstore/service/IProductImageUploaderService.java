package com.techstore.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Set;

public interface IProductImageUploaderService {
    Set<String> upload(Collection<MultipartFile> images, String productName);

    void deleteImagesForProduct(Collection<String> imageUrls);
}
