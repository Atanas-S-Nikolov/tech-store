package com.techstore.service.order;

import com.techstore.exception.order.OrderNotFoundException;
import com.techstore.exception.order.OrderServiceException;
import com.techstore.model.dto.OrderDto;
import com.techstore.model.entity.CartEntity;
import com.techstore.model.entity.OrderEntity;
import com.techstore.model.entity.ProductToBuyEntity;
import com.techstore.model.entity.PurchasedProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.OrderResponse;
import com.techstore.model.response.PageResponse;
import com.techstore.repository.IOrderRepository;
import com.techstore.repository.IPurchasedProductRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.service.cart.ICartService;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.techstore.model.enums.OrderStatus.CREATED;
import static com.techstore.model.enums.OrderStatus.DELIVERED;
import static com.techstore.model.enums.OrderStatus.RECEIVED;
import static com.techstore.model.enums.OrderStatus.RETURNED;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class OrderService implements IOrderService {
    private final IOrderRepository repository;
    private final IUserRepository userRepository;
    private final ICartService cartService;
    private final IPurchasedProductRepository purchasedProductRepository;

    public OrderService(IOrderRepository repository, IUserRepository userRepository, ICartService cartService,
                        IPurchasedProductRepository purchasedProductRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.purchasedProductRepository = purchasedProductRepository;
    }

    @Transactional
    @Override
    public OrderResponse createOrder(OrderDto orderDto) {
        String cartKey = orderDto.getCartKey();
        UserEntity userEntity = findUserByUsername(orderDto.getUsername());
        CartEntity cart = cartService.findCartEntity(cartKey);
        Set<PurchasedProductEntity> purchasedProductEntities = persistPurchasedProducts(cart.getProductsToBuy());
        OrderEntity persistedOrder = repository.save(
                new OrderEntity(null, userEntity, cartKey, purchasedProductEntities, cart.getTotalPrice(), LocalDateTime.now(), CREATED));
        setRelations(purchasedProductEntities, persistedOrder);
        cartService.decreaseStocks(cart.getProductsToBuy());
        cartService.deleteCart(orderDto.getCartKey());
        return toResponse(persistedOrder);
    }

    @Override
    public OrderResponse getOrder(OrderDto orderDto) {
        return toResponse(findOrder(orderDto));
    }

    @Override
    public PageResponse<OrderResponse> getAllOrders(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Page<OrderEntity> ordersPage = repository.findAllBetweenDates(startDate, endDate, PageRequest.of(page, size));
        List<OrderResponse> orders = ordersPage.getContent().stream().map(ModelConverter::toResponse).collect(toList());
        return new PageResponse<>(ordersPage.getTotalElements(), ordersPage.getTotalPages(), ordersPage.getNumber(), orders);
    }

    @Override
    public PageResponse<OrderResponse> getAllOrdersForUser(String username, LocalDateTime startDate, LocalDateTime endDate,
                                                           int page, int size) {
        Page<OrderEntity> ordersPage = repository.findAllByUsernameBetweenDates(username, startDate, endDate,
                PageRequest.of(page, size));
        List<OrderResponse> orders = ordersPage.getContent().stream().map(ModelConverter::toResponse).collect(toList());
        return new PageResponse<>(ordersPage.getTotalElements(), ordersPage.getTotalPages(), ordersPage.getNumber(), orders);
    }

    @Transactional
    @Override
    public OrderResponse deliverOrder(OrderDto orderDto) {
        OrderEntity order = findOrder(orderDto);
        order.setStatus(DELIVERED);
        return toResponse(repository.save(order));
    }

    @Transactional
    @Override
    public OrderResponse returnOrder(OrderDto orderDto) {
        OrderEntity order = findOrder(orderDto);
        cartService.increaseStocks(order.getPurchasedProducts());
        order.setStatus(RETURNED);
        return toResponse(repository.save(order));
    }

    @Transactional
    @Override
    public OrderResponse finalizeOrder(OrderDto orderDto) {
        OrderEntity order = findOrder(orderDto);
        order.setStatus(RECEIVED);
        return toResponse(repository.save(order));
    }

    @Transactional
    @Override
    public void deleteOrder(OrderDto orderDto) {
        OrderEntity order = findOrder(orderDto);
        deletePurchasedProducts(order.getPurchasedProducts());
        repository.delete(order);
    }

    @Transactional
    @Override
    public void deleteOrdersForUser(String username) {
        Collection<OrderEntity> allOrders = repository.findAllByUsername(username);
        allOrders.forEach(order -> {
            deletePurchasedProducts(order.getPurchasedProducts());
            repository.delete(order);
        });
    }

    private OrderEntity findOrder(OrderDto orderDto) {
        return repository.findByUsernameAndCartKey(orderDto.getUsername(), orderDto.getCartKey())
                .orElseThrow(() -> new OrderNotFoundException("Order is not found"));
    }

    private Set<PurchasedProductEntity> persistPurchasedProducts(Set<ProductToBuyEntity> toBuyEntities) {
        return toBuyEntities.stream().map(productToBuy -> purchasedProductRepository.save(
                new PurchasedProductEntity(null, productToBuy.getQuantity(), productToBuy.getProduct(), null)
        )).collect(toSet());
    }

    private void setRelations(Set<PurchasedProductEntity> purchasedProducts, OrderEntity order) {
        purchasedProducts.forEach(purchasedProduct -> {
            purchasedProduct.setOrder(order);
            purchasedProductRepository.save(purchasedProduct);
        });
    }

    private void deletePurchasedProducts(Set<PurchasedProductEntity> purchasedProducts) {
        purchasedProducts.forEach(purchasedProductRepository::delete);
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new OrderServiceException("Order failed because of invalid credentials"));
    }
}
