package com.techstore.service.product;

import com.techstore.exception.product.ProductImageUploaderServiceException;
import com.techstore.model.dto.ImageDto;
import com.techstore.model.response.ImageResponse;

import java.util.Collection;
import java.util.Set;

public interface IProductImageUploaderService {
    Set<ImageResponse> upload(Collection<ImageDto> images, String productName) throws ProductImageUploaderServiceException;

    void deleteImagesForProduct(Collection<String> imageUrls) throws ProductImageUploaderServiceException;

    Set<String> getImageUrlsForProduct(String productName);
}
