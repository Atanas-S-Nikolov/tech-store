package com.techstore.configuration;

import com.techstore.repository.ICartRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.cart.CartService;
import com.techstore.service.cart.ICartService;
import com.techstore.service.product.IProductImageUploaderService;
import com.techstore.service.product.IProductService;
import com.techstore.service.product.ProductImageUploaderService;
import com.techstore.service.product.ProductService;

import com.techstore.service.user.IUserService;
import com.techstore.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {
    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Bean("password-encoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean("image-uploader-service")
    IProductImageUploaderService imageUploaderService() {
        return new ProductImageUploaderService(bucketName);
    }

    @Bean("product-service")
    public IProductService productService() {
        return new ProductService(productRepository, imageUploaderService());
    }

    @Bean("user-service")
    public IUserService userService() {
        return new UserService(userRepository, cartRepository, passwordEncoder());
    }

    @Bean("cart-service")
    public ICartService cartService() {
        return new CartService(cartRepository, userRepository, productRepository);
    }
}
