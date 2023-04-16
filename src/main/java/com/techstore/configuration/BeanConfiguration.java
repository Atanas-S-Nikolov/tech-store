package com.techstore.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.techstore.repository.ICartRepository;
import com.techstore.repository.IFavoritesRepository;
import com.techstore.repository.IOrderRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IProductToBuyRepository;
import com.techstore.repository.IPurchasedProductRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.cart.CartService;
import com.techstore.service.cart.ICartService;
import com.techstore.service.favorites.FavoritesService;
import com.techstore.service.favorites.IFavoritesService;
import com.techstore.service.order.IOrderService;
import com.techstore.service.order.OrderService;
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

import static java.time.format.DateTimeFormatter.ofPattern;

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

    @Autowired
    private IFavoritesRepository favoritesRepository;

    @Autowired
    private IProductToBuyRepository productToBuyRepository;

    @Autowired
    private IPurchasedProductRepository purchasedProductRepository;

    @Autowired
    private IOrderRepository orderRepository;

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

    @Bean("cart-service")
    public ICartService cartService() {
        return new CartService(cartRepository, productRepository, productToBuyRepository);
    }

    @Bean("favorites-service")
    public IFavoritesService favoritesService() {
        return new FavoritesService(favoritesRepository, userRepository, productRepository);
    }

    @Bean("user-service")
    public IUserService userService() {
        return new UserService(userRepository, passwordEncoder(), favoritesService(), orderService());
    }

    @Bean("order-service")
    public IOrderService orderService() {
        return new OrderService(orderRepository, userRepository, cartService(), purchasedProductRepository);
    }

    @Bean("object-mapper")
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(ofPattern("yyyy-MM-dd hh:mm:ss"));
        module.addSerializer(dateTimeSerializer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.coercionConfigFor(LogicalType.Enum).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        return mapper;
    }
}
