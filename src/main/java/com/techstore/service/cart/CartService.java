package com.techstore.service.cart;

import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.exception.product.CannotBuyProductException;
import com.techstore.exception.product.ProductNotFoundException;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.lang.String.format;

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
        return repository.save(new CartEntity(null, null, new HashSet<>(), BigDecimal.ZERO));
    }

    @Override
    public Cart getCart(String username) {
        return toModel(findCart(username));
    }

    @Transactional
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

        return toModel(repository.save(cartEntity));
    }

    @Transactional
    @Override
    public Cart removeProductFromCart(CartDto cartDto) {
        String username = cartDto.getUsername();
        ProductToBuyDto toBuyDto = cartDto.getProductsToBuy().iterator().next();
        CartEntity cartEntity = findCart(username);

        String productName = toBuyDto.getProductName();
        ProductEntity productEntity = findProductEntityByName(productName);
        Optional<ProductToBuyEntity> optionalToBuyEntity = productToBuyRepository.findProductToBuyByName(productName);

        if (optionalToBuyEntity.isPresent()) {
            ProductToBuyEntity toBuyEntity = optionalToBuyEntity.get();
            Set<ProductToBuyEntity> persistedToBuyEntities = cartEntity.getProductsToBuy();
            persistedToBuyEntities.remove(toBuyEntity);
            cartEntity.setProductsToBuy(persistedToBuyEntities);
            cartEntity.setTotalPrice(calculateTotalPrice(persistedToBuyEntities));
            toBuyEntity.setProduct(null);
            productEntity.setProductToBuy(null);
        }

        return toModel(repository.save(cartEntity));
    }

    @Transactional
    @Override
    public Cart doPurchase(String username) {
        CartEntity cartEntity = findCart(username);
        Set<ProductToBuyEntity> products = cartEntity.getProductsToBuy();
        decreaseStocks(products);
        return clearCartDetails(cartEntity, products);
    }

    @Transactional
    @Override
    public Cart clearCart(String username) {
        CartEntity cartEntity = findCart(username);
        return clearCartDetails(cartEntity, cartEntity.getProductsToBuy());
    }

    @Transactional
    @Override
    public void deleteCart(String username) {
        CartEntity cartEntity = findCart(username);
        deleteProductsToBuy(cartEntity.getProductsToBuy());
        repository.delete(cartEntity);
    }

    private void deleteProductsToBuy(Set<ProductToBuyEntity> productToBuyEntities) {
        productToBuyEntities.forEach(productToBuyRepository::delete);
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
        String productName = toBuyDto.getProductName();
        int quantity = toBuyDto.getQuantity();
        ProductEntity productEntity = findProductEntityByName(productName);
        if (quantity > productEntity.getStocks()) {
            throw new CannotBuyProductException("We currently do not have enough stocks for this product");
        }
        ProductToBuyEntity toBuyEntity = productToBuyRepository.findProductToBuyByName(productName)
                .orElseGet(ProductToBuyEntity::new);
        toBuyEntity.setQuantity(quantity);
        toBuyEntity.setProduct(productEntity);
        toBuyEntity = productToBuyRepository.save(toBuyEntity);
        productEntity.setProductToBuy(toBuyEntity);
        productRepository.save(productEntity);
        return toBuyEntity;
    }

    private ProductEntity findProductEntityByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with name '%s' is not found", name)));
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

    private void decreaseStocks(Set<ProductToBuyEntity> productsToBuy) {
        productsToBuy.forEach(productToBuy -> {
            ProductEntity product = productToBuy.getProduct();
            product.setStocks(product.getStocks() - productToBuy.getQuantity());
            productRepository.save(product);
        });
    }

    private Cart clearCartDetails(CartEntity cartEntity, Set<ProductToBuyEntity> productsToDelete) {
        cartEntity.setTotalPrice(BigDecimal.ZERO);
        cartEntity.setProductsToBuy(new HashSet<>());
        CartEntity persistedEntity = repository.save(cartEntity);
        deleteProductsToBuy(productsToDelete);
        return toModel(persistedEntity);
    }
}
