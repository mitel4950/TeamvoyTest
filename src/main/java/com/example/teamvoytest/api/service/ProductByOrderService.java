package com.example.teamvoytest.api.service;

import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.domain.model.ProductByOrder;
import java.util.List;
import java.util.Set;

public interface ProductByOrderService {

  void saveAll(List<ProductForOrderRequest> products, Long orderId);

  List<ProductByOrder> listEntitiesByOrderId(Long orderId);

  List<ProductByOrder> listEntitiesByOrderIds(Set<Long> orderIds);

  List<ProductByOrder> listActiveEntitiesByProductIds(Set<Long> productIds);
}
