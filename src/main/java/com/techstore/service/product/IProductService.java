package com.techstore.service.product;

import java.util.Collection;
import java.util.List;

import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    ProductResponse createProduct(ProductDto productDto, Collection<MultipartFile> images);

    ProductResponse getProduct(String productName) throws ProductNotFoundException;

    PageResponse<ProductResponse> getProducts(boolean earlyAccess, String category, String type, int page, int size);

    List<ProductResponse> search(String keyword, boolean earlyAccess);

    PageResponse<ProductResponse> searchQuery(String keyword, boolean earlyAccess, int page, int size);

    ProductResponse updateProduct(ProductDto productDto, Collection<MultipartFile> images) throws ProductNotFoundException;

    void deleteProduct(String productName) throws ProductNotFoundException;
}
