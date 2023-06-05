package com.techstore.controller;

import com.techstore.model.dto.OrderDto;
import com.techstore.model.dto.UsernameDto;
import com.techstore.model.response.OrderResponse;
import com.techstore.model.response.PageResponse;
import com.techstore.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.techstore.constants.ApiConstants.ALL_URL;
import static com.techstore.constants.ApiConstants.CANCEL_URL;
import static com.techstore.constants.ApiConstants.DELIVER_URL;
import static com.techstore.constants.ApiConstants.FINALIZE_URL;
import static com.techstore.constants.ApiConstants.GET_URL;
import static com.techstore.constants.ApiConstants.ORDERS_URL;
import static com.techstore.constants.ApiConstants.ORDER_END_DATE_PARAM;
import static com.techstore.constants.ApiConstants.ORDER_START_DATE_PARAM;
import static com.techstore.constants.ApiConstants.PAGE_PARAM;
import static com.techstore.constants.ApiConstants.PAGE_PARAM_DEFAULT_VALUE;
import static com.techstore.constants.ApiConstants.RETURN_URL;
import static com.techstore.constants.ApiConstants.SIZE_PARAM;
import static com.techstore.constants.ApiConstants.SIZE_PARAM_DEFAULT_VALUE;
import static com.techstore.constants.DateConstants.LOCAL_DATE_TIME_EPOCH;
import static com.techstore.constants.DateConstants.LOCAL_DATE_TIME_NOW_SpEL;
import static com.techstore.constants.DateConstants.LOCAL_DATE_TIME_PRECISION_FORMAT;
import static com.techstore.constants.RoleConstants.HAS_ROLE_ADMIN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "order-controller")
@RequestMapping(value = ORDERS_URL)
public class OrderController {
    private final IOrderService service;

    @Autowired
    public OrderController(IOrderService service) {
        this.service = service;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(CREATED).body(service.createOrder(orderDto));
    }

    @PostMapping(path = GET_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(OK).body(service.getOrder(orderDto));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @GetMapping(path = ALL_URL, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(value = ORDER_START_DATE_PARAM, required = false, defaultValue = LOCAL_DATE_TIME_EPOCH)
                    String startDate,
            @RequestParam(value = ORDER_END_DATE_PARAM, required = false, defaultValue = LOCAL_DATE_TIME_NOW_SpEL)
                    String endDate,
            @RequestParam(value = PAGE_PARAM, defaultValue = PAGE_PARAM_DEFAULT_VALUE) int page,
            @RequestParam(value = SIZE_PARAM, defaultValue = SIZE_PARAM_DEFAULT_VALUE) int size
    ) {
        return ResponseEntity.status(OK).body(service.getAllOrders(startDate, endDate, page, size));
    }

    @PostMapping(path = ALL_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrdersForUser(
            @RequestBody @Valid UsernameDto usernameDto,
            @RequestParam(value = ORDER_START_DATE_PARAM, required = false, defaultValue = LOCAL_DATE_TIME_EPOCH)
            @DateTimeFormat(pattern = LOCAL_DATE_TIME_PRECISION_FORMAT)
                    String startDate,
            @RequestParam(value = ORDER_END_DATE_PARAM, required = false, defaultValue = LOCAL_DATE_TIME_NOW_SpEL)
            @DateTimeFormat(pattern = LOCAL_DATE_TIME_PRECISION_FORMAT)
                    String endDate,
            @RequestParam(value = PAGE_PARAM, defaultValue = PAGE_PARAM_DEFAULT_VALUE) int page,
            @RequestParam(value = SIZE_PARAM, defaultValue = SIZE_PARAM_DEFAULT_VALUE) int size
    ) {
        return ResponseEntity.status(OK).body(service.getAllOrdersForUser(usernameDto.getUsername(), startDate, endDate, page, size));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping(path = DELIVER_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> deliverOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(OK).body(service.deliverOrder(orderDto));
    }

    @PutMapping(path = CANCEL_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> cancelOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(OK).body(service.cancelOrder(orderDto));
    }

    @PutMapping(path = RETURN_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> returnOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(OK).body(service.returnOrder(orderDto));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping(path = FINALIZE_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> finalizeOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.status(OK).body(service.finalizeOrder(orderDto));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteOrder(@RequestBody @Valid OrderDto orderDto) {
        service.deleteOrder(orderDto);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
