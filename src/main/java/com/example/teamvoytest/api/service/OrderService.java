package com.example.teamvoytest.api.service;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import java.util.List;

public interface OrderService {

  List<OrderResponse> listOrders();

  OrderResponse getOrderById(long orderId);


  OrderResponse createOrder(CreateOrderRequest order);

  void cancelOrder(long orderId);

  void confirmPayment(long orderId);

}
