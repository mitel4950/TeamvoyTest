package com.example.teamvoytest.service;

import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.domain.mapper.ProductByOrderMapper;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.repository.ProductByOrderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductByOrderServiceImpl implements ProductByOrderService {

  @Value("${custom.order-activity-time-milliseconds}")
  private Long orderActivityTimeMilliseconds;

  private final ProductByOrderRepository repository;
  private final ProductByOrderMapper mapper;

  @Override
  public void saveAll(List<ProductForOrderRequest> products, Long orderId) {
    List<ProductByOrder> pboList = mapper.dtoListToEntityList(products, orderId);
    repository.saveAll(pboList);
  }

  @Override
  public List<ProductByOrder> listEntitiesByOrderId(Long orderId) {
    return repository.findAllById_OrderId(orderId);
  }

  @Override
  public List<ProductByOrder> listEntitiesByOrderIds(Set<Long> orderIds) {
    return repository.findAllById_OrderIdIn(orderIds);
  }

  @Override
  public List<ProductByOrder> listActiveEntitiesByProductIds(Set<Long> productIds) {
    OffsetDateTime time = LocalDateTime.now()
        .minus(Duration.ofMillis(orderActivityTimeMilliseconds))
        .atOffset(ZoneOffset.UTC);

    return repository.findRecentOrdersWithProductIds(productIds, time, OrderStatus.AWAITING);
  }
}
