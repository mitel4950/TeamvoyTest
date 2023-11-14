package com.example.teamvoytest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.domain.mapper.ProductByOrderMapper;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.model.composite_key.ProductByOrderId;
import com.example.teamvoytest.domain.repository.ProductByOrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductByOrderServiceImplTest {

  @Mock
  private ProductByOrderRepository repository;

  @Mock
  private ProductByOrderMapper mapper;

  @InjectMocks
  private ProductByOrderServiceImpl service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "orderActivityTimeMilliseconds", 1000L);
  }

  @Test
  void saveAll_ShouldSaveProductByOrders() {
    List<ProductForOrderRequest> productForOrderRequests = List.of(
        new ProductForOrderRequest(1L, 2, null),
        new ProductForOrderRequest(2L, 3, null)
    );
    ArrayList<ProductByOrder> pboList = new ArrayList<>();
    pboList.add(new ProductByOrder(new ProductByOrderId(2L, 1L), 2, 5));
    pboList.add(new ProductByOrder(new ProductByOrderId(1L, 2L), 3, 40));

    when(mapper.dtoListToEntityList(productForOrderRequests, 1L)).thenReturn(pboList);

    service.saveAll(productForOrderRequests, 1L);

    verify(repository).saveAll(pboList);
  }

  @Test
  void listEntitiesByOrderId_ShouldReturnProductByOrders() {
    Long orderId = 1L;
    List<ProductByOrder> pboList = List.of(
        new ProductByOrder(new ProductByOrderId(2L, 1L), 2, 5),
        new ProductByOrder(new ProductByOrderId(1L, 2L), 3, 40)
    );

    when(repository.findAllById_OrderId(orderId)).thenReturn(pboList);

    List<ProductByOrder> result = service.listEntitiesByOrderId(orderId);

    assertEquals(pboList, result);
    verify(repository).findAllById_OrderId(orderId);
  }

  @Test
  void listEntitiesByOrderIds_ShouldReturnProductByOrders() {
    Set<Long> orderIds = Set.of(1L, 2L);
    List<ProductByOrder> pboList = List.of(
        new ProductByOrder(new ProductByOrderId(2L, 1L), 2, 5),
        new ProductByOrder(new ProductByOrderId(1L, 2L), 3, 40)
    );

    when(repository.findAllById_OrderIdIn(orderIds)).thenReturn(pboList);

    List<ProductByOrder> result = service.listEntitiesByOrderIds(orderIds);

    assertEquals(pboList, result);
    verify(repository).findAllById_OrderIdIn(orderIds);
  }

  @Test
  void listActiveEntitiesByProductIds_ShouldReturnActiveProductByOrders() {
    Set<Long> productIds = Set.of(1L, 2L);
    List<ProductByOrder> pboList = List.of(
        new ProductByOrder(new ProductByOrderId(2L, 1L), 2, 5),
        new ProductByOrder(new ProductByOrderId(1L, 2L), 3, 40)
    );

    when(repository.findRecentOrdersWithProductIds(eq(productIds), any(),
                                                   eq(OrderStatus.AWAITING))).thenReturn(pboList);

    List<ProductByOrder> result = service.listActiveEntitiesByProductIds(productIds);

    assertEquals(pboList, result);
    verify(repository).findRecentOrdersWithProductIds(eq(productIds), any(),
                                                      eq(OrderStatus.AWAITING));
  }
}
