package com.example.teamvoytest.api.service;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderService {

  Page<OrderResponse> listOrders(boolean includeProducts, PageRequest pageable);

  OrderResponse getOrderById(long orderId, boolean includeProduct);

  OrderResponse createOrderWithProductSync(CreateOrderRequest order);

  void cancelOrder(long orderId);

  void confirmPayment(long orderId);

}
