package com.example.teamvoytest.controller;

import com.example.teamvoytest.api.controller.OrderApi;
import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import com.example.teamvoytest.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

  private final OrderService service;

  @Override
  public Page<OrderResponse> listOrders(boolean includeProducts, int pageNumber, int pageSize) {
    return service.listOrders(includeProducts, PageRequest.of(pageNumber, pageSize));
  }

  @Override
  public OrderResponse getOrderById(long orderId, boolean includeProduct) {
    return service.getOrderById(orderId, includeProduct);
  }

  @Override
  public OrderResponse createOrder(CreateOrderRequest order) {
    return service.createOrderWithProductSync(order);
  }

  @Override
  public void cancelOrder(long orderId) {
    service.cancelOrder(orderId);
  }

  @Override
  public void confirmPayment(long orderId) {
    service.confirmPayment(orderId);
  }
}
