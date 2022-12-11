package com.techstore.service.cart;

import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.exception.cart.CartServiceException;
import com.techstore.model.Cart;
import com.techstore.model.ProductToBuy;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.ProductToBuyDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.repository.ICartRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IUserRepository;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.techstore.utils.converter.ModelConverter.convertProductToProductToBuy;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

public class CartService implements ICartService {
    private final ICartRepository repository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public CartService(ICartRepository repository, IUserRepository userRepository, IProductRepository productRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart getCart(String username) {
        return toModel(findCart(username));
    }

    @Override
    public Cart addProductsToCart(CartDto cartDto) {
        String username = cartDto.getUsername();
        Set<ProductToBuyDto> toBuyDtos = cartDto.getProductsToBuy();
        CartEntity cartEntity = findCart(username);

        Set<ProductEntity> productEntities = cartEntity.getProducts();
        toBuyDtos.forEach(productToBuy -> {
                productRepository.findProductByName(productToBuy.getProductName()).ifPresent(productEntities::add);
        });
        cartEntity.setProducts(productEntities);

        Set<ProductToBuy> products = convertToProductsToBuy(productEntities, toBuyDtos);
        cartEntity.setTotalPrice(calculateTotalPrice(products));
        return toModel(executeDBCall(() -> repository.save(cartEntity)));
    }

    @Override
    public void deleteCart(String username) {
        executeDBCall(() -> repository.delete(findCart(username)));
    }

    private CartEntity findCart(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findUserByUsername(username);
        CartEntity entity = new CartEntity();
        if (userEntityOptional.isPresent()) {
            entity = repository.findByUser(userEntityOptional.get())
                    .orElseThrow(() -> new CartNotFoundException(format("Cart for user with username: '%s' is not found", username)));
        }
        return entity;
    }

    private Set<ProductToBuy> convertToProductsToBuy(Set<ProductEntity> productEntities, Set<ProductToBuyDto> toBuyDtos) {
        return productEntities.stream().map(product -> {
            long quantity = 0L;
            for (ProductToBuyDto toBuyDto : toBuyDtos) {
                if (toBuyDto.getProductName().equals(product.getName())) {
                    quantity = toBuyDto.getQuantity();
                }
            }
            return convertProductToProductToBuy(product, quantity);
        }).collect(toSet());
    }

    private BigDecimal calculateTotalPrice(Set<ProductToBuy> productsToBuy) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductToBuy productToBuy : productsToBuy) {
            BigDecimal price = productToBuy.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(productToBuy.getQuantity());
            totalPrice = totalPrice.add(price.multiply(quantity));
        }
        return totalPrice;
    }

    private <T> T executeDBCall(Supplier<T> supplier) {
        try{
            return supplier.get();
        } catch (DataAccessException dataAccessException) {
            throw new CartServiceException("Error while connecting the database", dataAccessException);
        }
    }

    private void executeDBCall(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataAccessException dataAccessException) {
            throw new CartServiceException("Error while connecting the database", dataAccessException);
        }
    }
}
