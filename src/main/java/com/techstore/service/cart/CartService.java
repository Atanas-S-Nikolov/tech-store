package com.techstore.service.cart;

import com.techstore.exception.cart.CartConstraintViolationException;
import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.model.Cart;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.ProductToBuyDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.repository.ICartRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IProductToBuyRepository;
import com.techstore.repository.IUserRepository;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

public class CartService implements ICartService {
    private final ICartRepository repository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IProductToBuyRepository productToBuyRepository;

    public CartService(ICartRepository repository, IUserRepository userRepository, IProductRepository productRepository, IProductToBuyRepository productToBuyRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productToBuyRepository = productToBuyRepository;
    }

    @Override
    public CartEntity createDefaultCart() {
        return executeDBCall(() -> repository.save(new CartEntity(null, null, new HashSet<>(), BigDecimal.ZERO)));
    }

    @Override
    public Cart getCart(String username) {
        return toModel(findCart(username));
    }

    @Override
    public Cart addProductToCart(CartDto cartDto) {
        String username = cartDto.getUsername();
        ProductToBuyDto toBuyDto = cartDto.getProductsToBuy().iterator().next();
        CartEntity cartEntity = findCart(username);

        ProductToBuyEntity toBuyEntity = persistProductToBuy(toBuyDto);
        Set<ProductToBuyEntity> entities = cartEntity.getProductsToBuy();
        entities.add(toBuyEntity);
        cartEntity.setProductsToBuy(entities);
        cartEntity.setTotalPrice(calculateTotalPrice(entities));

        return toModel(executeDBCall(() -> repository.save(cartEntity)));
    }

    @Transactional
    @Override
    public Cart updateCart(CartDto cartDto) {
        String username = cartDto.getUsername();
        Set<ProductToBuyDto> toBuyDtos = cartDto.getProductsToBuy();
        CartEntity cartEntity = findCart(username);
        Set<ProductToBuyEntity> toBuyEntities = toBuyDtos.stream()
                .map(this::persistProductToBuy)
                .filter(Objects::nonNull)
                .collect(toSet());
        cartEntity.setProductsToBuy(toBuyEntities);
        cartEntity.setTotalPrice(calculateTotalPrice(toBuyEntities));
        return toModel(executeDBCall(() -> repository.save(cartEntity)));
    }

    @Transactional
    @Override
    public Cart clearCart(String username) {
        CartEntity cartEntity = findCart(username);
        Set<ProductToBuyEntity> productsToDelete = cartEntity.getProductsToBuy();
        cartEntity.setTotalPrice(BigDecimal.ZERO);
        cartEntity.setProductsToBuy(new HashSet<>());
        CartEntity persistedEntity = executeDBCall(() -> repository.save(cartEntity));
        deleteProductsToBuy(productsToDelete);
        return toModel(persistedEntity);
    }

    @Transactional
    @Override
    public void deleteCart(String username) {
        CartEntity cartEntity = findCart(username);
        deleteProductsToBuy(cartEntity.getProductsToBuy());
        executeDBCall(() -> repository.delete(cartEntity));
    }

    private void deleteProductsToBuy(Set<ProductToBuyEntity> productToBuyEntities) {
        executeDBCall(() -> productToBuyEntities.forEach(productToBuyRepository::delete));
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

    private ProductToBuyEntity persistProductToBuy(ProductToBuyDto toBuyDto) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findProductByName(toBuyDto.getProductName());
        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            ProductToBuyEntity toBuyEntity = productToBuyRepository.findProductToBuyByProduct(productEntity)
                    .orElseGet(ProductToBuyEntity::new);
            toBuyEntity.setQuantity(toBuyDto.getQuantity());
            toBuyEntity.setProduct(productEntity);
            toBuyEntity = productToBuyRepository.save(toBuyEntity);
            productEntity.setProductToBuy(toBuyEntity);
            productRepository.save(productEntity);
            return toBuyEntity;
        }
        return null;
    }

    private BigDecimal calculateTotalPrice(Set<ProductToBuyEntity> productsToBuy) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductToBuyEntity toBuyEntity : productsToBuy) {
            BigDecimal price = toBuyEntity.getProduct().getPrice();
            BigDecimal quantity = new BigDecimal(toBuyEntity.getQuantity());
            totalPrice = totalPrice.add(price.multiply(quantity));
        }
        return totalPrice;
    }

    private <T> T executeDBCall(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new CartConstraintViolationException("Cart constraint violation");
        }
    }

    private void executeDBCall(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new CartConstraintViolationException("Cart constraint violation");
        }
    }
}
