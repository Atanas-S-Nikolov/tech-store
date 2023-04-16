package com.techstore.service.order;

import com.techstore.model.dto.OrderDto;
import com.techstore.model.response.OrderResponse;
import com.techstore.model.response.PageResponse;

import java.time.LocalDateTime;

public interface IOrderService {
    OrderResponse createOrder(OrderDto orderDto);

    OrderResponse getOrder(OrderDto orderDto);

    PageResponse<OrderResponse> getAllOrders(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    PageResponse<OrderResponse> getAllOrdersForUser(String username, LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    OrderResponse deliverOrder(OrderDto orderDto);

    OrderResponse returnOrder(OrderDto orderDto);

    OrderResponse finalizeOrder(OrderDto orderDto);

    void deleteOrder(OrderDto orderDto);

    void deleteOrdersForUser(String username);
}
