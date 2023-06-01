package com.techstore.service.cart;

import com.techstore.exception.cart.CartNotFoundException;
import com.techstore.exception.product.CannotBuyProductException;
import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.dto.CartDto;
import com.techstore.model.dto.QuickOrderDto;
import com.techstore.model.entity.PurchasedProductEntity;
import com.techstore.model.response.CartResponse;
import com.techstore.model.dto.UpdateCartDto;
import com.techstore.model.dto.ProductToBuyDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.repository.ICartRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IProductToBuyRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.lang.String.format;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toSet;

public class CartService implements ICartService {
    private final ICartRepository repository;
    private final IProductRepository productRepository;
    private final IProductToBuyRepository productToBuyRepository;

    public CartService(ICartRepository repository, IProductRepository productRepository, IProductToBuyRepository productToBuyRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.productToBuyRepository = productToBuyRepository;
    }

    @Transactional
    @Override
    public CartResponse createCart(CartDto cartDto) {
        Set<ProductToBuyEntity> productToBuyEntities = cartDto.getProductsToBuy().stream()
                .map(this::persistProductToBuy)
                .collect(toSet());
        CartEntity entity = new CartEntity(null, Long.toString(now().toEpochMilli()), productToBuyEntities,
                calculateTotalPrice(productToBuyEntities));
        return toResponse(repository.save(entity));
    }

    @Override
    public CartResponse getCart(String key) {
        return toResponse(findCartEntity(key));
    }

    @Transactional
    @Override
    public CartResponse addProductToCart(UpdateCartDto updateCartDto) {
        String key = updateCartDto.getCartKey();
        ProductToBuyDto toBuyDto = updateCartDto.getProductsToBuy().iterator().next();
        CartEntity cartEntity = findCartEntity(key);

        ProductToBuyEntity toBuyEntity = persistProductToBuy(toBuyDto);
        Set<ProductToBuyEntity> entities = cartEntity.getProductsToBuy();
        entities.add(toBuyEntity);
        cartEntity.setProductsToBuy(entities);
        cartEntity.setTotalPrice(calculateTotalPrice(entities));

        return toResponse(repository.save(cartEntity));
    }

    @Transactional
    @Override
    public CartResponse removeProductFromCart(UpdateCartDto updateCartDto) {
        String key = updateCartDto.getCartKey();
        ProductToBuyDto toBuyDto = updateCartDto.getProductsToBuy().iterator().next();
        CartEntity cartEntity = findCartEntity(key);

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
            productToBuyRepository.delete(toBuyEntity);
        }

        return toResponse(repository.save(cartEntity));
    }

    @Transactional
    @Override
    public CartResponse doPurchase(String key, QuickOrderDto quickOrderDto) {
        CartEntity cartEntity = findCartEntity(key);
        Set<ProductToBuyEntity> products = cartEntity.getProductsToBuy();
        decreaseStocks(products);
        // TODO: email confirmation should be done with https://github.com/Atanas-S-Nikolov/tech-store/issues/3
        deleteCart(key);
        cartEntity.setTotalPrice(BigDecimal.ZERO);
        cartEntity.setProductsToBuy(new HashSet<>());
        return toResponse(cartEntity);
    }

    @Transactional
    @Override
    public void deleteCart(String key) {
        CartEntity cartEntity = findCartEntity(key);
        deleteProductsToBuy(cartEntity.getProductsToBuy());
        repository.delete(cartEntity);
    }

    @Override
    public CartEntity findCartEntity(String key) {
        return repository.findByKey(key).orElseThrow(() ->
                new CartNotFoundException(format("Cart with key: '%s' is not found", key)));
    }

    @Transactional
    @Override
    public void decreaseStocks(Set<ProductToBuyEntity> productsToBuy) {
        productsToBuy.forEach(productToBuy -> {
            ProductEntity product = productToBuy.getProduct();
            product.setStocks(product.getStocks() - productToBuy.getQuantity());
            productRepository.save(product);
        });
    }

    @Transactional
    @Override
    public void increaseStocks(Set<PurchasedProductEntity> purchasedProducts) {
        purchasedProducts.forEach(productToBuy -> {
            ProductEntity product = productToBuy.getProduct();
            product.setStocks(product.getStocks() + productToBuy.getQuantity());
            productRepository.save(product);
        });
    }

    private void deleteProductsToBuy(Set<ProductToBuyEntity> productToBuyEntities) {
        productToBuyEntities.forEach(productToBuyRepository::delete);
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
}
