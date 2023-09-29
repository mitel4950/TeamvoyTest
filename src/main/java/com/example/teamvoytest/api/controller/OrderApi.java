package com.example.teamvoytest.api.controller;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public interface OrderApi {

  @GetMapping
  List<OrderResponse> listOrders();

  @GetMapping("/{orderId}")
  OrderResponse getOrderById(@PathVariable("orderId") long orderId);

  @PostMapping
  OrderResponse createOrder(@RequestBody CreateOrderRequest order);

  @DeleteMapping("/{orderId}")
  void cancelOrder(@PathVariable("orderId") long orderId);

  @PutMapping("/{orderId}/confirm-payment")
  void confirmPayment(@PathVariable("orderId") long orderId);
}
