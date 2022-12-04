package com.techstore.configuration;

import com.techstore.repository.IProductRepository;
import com.techstore.service.product.IProductImageUploaderService;
import com.techstore.service.product.IProductService;
import com.techstore.service.product.ProductImageUploaderService;
import com.techstore.service.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Autowired
    private IProductRepository productRepository;

    @Bean("image-uploader-service")
    IProductImageUploaderService imageUploaderService() {
        return new ProductImageUploaderService(bucketName);
    }

    @Bean("product-service")
    public IProductService productService() {
        return new ProductService(productRepository, imageUploaderService());
    }
}
