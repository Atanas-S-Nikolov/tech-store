package com.techstore.service.product;

import java.util.Collection;

import com.techstore.exception.product.ProductConstraintViolationException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Product createProduct(Product product, Collection<MultipartFile> images) throws ProductConstraintViolationException;

    Product getProduct(String productName) throws ProductConstraintViolationException, ProductNotFoundException;

    Collection<Product> getProducts(boolean earlyAccess, String category, String type) throws ProductConstraintViolationException;

    Product updateProduct(Product product, Collection<MultipartFile> images) throws ProductConstraintViolationException, ProductNotFoundException;

    void deleteProduct(String productName) throws ProductConstraintViolationException, ProductNotFoundException;
}
