package com.techstore.service.product;

import com.techstore.exception.product.ProductImageUploaderServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Set;

public interface IProductImageUploaderService {
    Set<String> upload(Collection<MultipartFile> images, String productName) throws ProductImageUploaderServiceException;

    void deleteImagesForProduct(Collection<String> imageUrls) throws ProductImageUploaderServiceException;

    Set<String> getImageUrlsForProduct(String productName);
}
