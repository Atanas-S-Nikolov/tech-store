package com.techstore.service.product;

import java.util.Collection;

import com.techstore.exception.product.ProductConstraintViolationException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.exception.product.ProductServiceException;
import com.techstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Product createProduct(Product product, Collection<MultipartFile> images) throws ProductConstraintViolationException, ProductServiceException;

    Product getProduct(String productName) throws ProductConstraintViolationException, ProductServiceException, ProductNotFoundException;

    Collection<Product> getProducts(boolean earlyAccess) throws ProductConstraintViolationException, ProductServiceException;

    Product updateProduct(Product product, Collection<MultipartFile> images) throws ProductConstraintViolationException, ProductServiceException, ProductNotFoundException;

    void deleteProduct(String productName) throws ProductConstraintViolationException, ProductServiceException, ProductNotFoundException;
}
