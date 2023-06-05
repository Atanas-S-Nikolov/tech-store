package com.techstore.service.order;

import com.techstore.model.dto.OrderDto;
import com.techstore.model.response.OrderResponse;
import com.techstore.model.response.PageResponse;

public interface IOrderService {
    OrderResponse createOrder(OrderDto orderDto);

    OrderResponse getOrder(OrderDto orderDto);

    PageResponse<OrderResponse> getAllOrders(String startDate, String endDate, int page, int size);

    PageResponse<OrderResponse> getAllOrdersForUser(String username, String startDate, String endDate, int page, int size);

    OrderResponse deliverOrder(OrderDto orderDto);

    OrderResponse cancelOrder(OrderDto orderDto);

    OrderResponse returnOrder(OrderDto orderDto);

    OrderResponse finalizeOrder(OrderDto orderDto);

    void deleteOrder(OrderDto orderDto);

    void deleteOrdersForUser(String username);
}
