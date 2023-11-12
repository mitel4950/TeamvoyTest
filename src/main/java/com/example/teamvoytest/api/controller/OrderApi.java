package com.example.teamvoytest.api.controller;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public interface OrderApi {

  @GetMapping
  Page<OrderResponse> listOrders(
      @RequestParam(required = false) boolean includeProducts,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize
  );

  @GetMapping("/{orderId}")
  OrderResponse getOrderById(
      @PathVariable("orderId") long orderId,
      @RequestParam(required = false) boolean includeProduct);

  @PostMapping
  OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest order);

  @DeleteMapping("/{orderId}")
  void cancelOrder(@PathVariable("orderId") long orderId);

  @PutMapping("/{orderId}/confirm-payment")
  void confirmPayment(@PathVariable("orderId") long orderId);
}
