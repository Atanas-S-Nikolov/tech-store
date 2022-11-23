package com.techstore.service;

import java.util.Collection;

import com.techstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Product createProduct(Product product, Collection<MultipartFile> images);

    Product getProduct(String productName);

    Collection<Product> getAllProducts();

    Product updateProduct(Product product, Collection<MultipartFile> images);

    void deleteProduct(String productName);
}
