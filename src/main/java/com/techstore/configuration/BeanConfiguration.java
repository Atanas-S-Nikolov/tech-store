package com.techstore.configuration;

import com.techstore.repository.IProductRepository;
import com.techstore.service.IProductImageUploaderService;
import com.techstore.service.IProductService;
import com.techstore.service.ProductImageUploaderService;
import com.techstore.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Value("${firebase.bucket-name}")
    private String BUCKET_NAME;

    @Autowired
    private IProductRepository productRepository;

    @Bean("image-uploader-service")
    IProductImageUploaderService imageUploaderService() {
        return new ProductImageUploaderService(BUCKET_NAME);
    }

    @Bean("product-service")
    public IProductService productService() {
        return new ProductService(productRepository, imageUploaderService());
    }
}
