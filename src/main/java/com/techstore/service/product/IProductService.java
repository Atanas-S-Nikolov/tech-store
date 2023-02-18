package com.techstore.service.product;

import java.util.Collection;

import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Product createProduct(Product product, Collection<MultipartFile> images);

    Product getProduct(String productName) throws ProductNotFoundException;

    Collection<Product> getProducts(boolean earlyAccess, String category, String type);

    Product updateProduct(Product product, Collection<MultipartFile> images) throws ProductNotFoundException;

    void deleteProduct(String productName) throws ProductNotFoundException;
}
