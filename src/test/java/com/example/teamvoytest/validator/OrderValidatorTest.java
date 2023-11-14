package com.example.teamvoytest.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.model.Order;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.model.composite_key.ProductByOrderId;
import com.example.teamvoytest.exception.InabilityToLinkOrderException;
import com.example.teamvoytest.exception.OrderStatusException;
import com.example.teamvoytest.exception.ProductStatusException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

  @Mock
  private ProductService productService;
  @Mock
  private ProductByOrderService productByOrderService;
  @InjectMocks
  private OrderValidator orderValidator;

  private Product product1;
  private ProductByOrder pbo1;
  private Order order;

  @BeforeEach
  void setUp() {
    product1 = new Product(1L, "Product 1", 1000, 8, ProductStatus.AVAILABLE);
    pbo1 = new ProductByOrder(new ProductByOrderId(1L, 1L), 5, 1000);
    order = new Order(1L, OrderStatus.AWAITING, OffsetDateTime.now().minus(Duration.ofDays(1)));
  }

  @Test
  void validateCreateOrderRequest_WithInvalidData_ShouldThrowInabilityToLinkOrderException() {
    CreateOrderRequest request = new CreateOrderRequest(
        List.of(new ProductForOrderRequest(1L, 5, null)));
    when(productService.getEntitiesByIds(Set.of(1L))).thenReturn(List.of(product1));
    when(productByOrderService.listActiveEntitiesByProductIds(Set.of(1L))).thenReturn(
        List.of(pbo1));

    assertThrows(InabilityToLinkOrderException.class,
                 () -> orderValidator.validateCreateOrderRequest(request));
  }

  @Test
  void validateCreateOrderRequest_WithInvalidData_ShouldThrowProductStatusException() {
    CreateOrderRequest request = new CreateOrderRequest(
        List.of(new ProductForOrderRequest(1L, 5, null)));

    product1.setStatus(ProductStatus.UNAVAILABLE);
    product1.setInventoryCount(0);

    when(productService.getEntitiesByIds(Set.of(1L))).thenReturn(List.of(product1));

    assertThrows(ProductStatusException.class,
                 () -> orderValidator.validateCreateOrderRequest(request));
  }

  @Test
  void validateOrderStatusToCancel_WithCompletedOrder_ShouldThrowException() {
    order.setStatus(OrderStatus.COMPLETED);

    assertThrows(OrderStatusException.class,
                 () -> OrderValidator.validateOrderStatusToCancel(order));
  }

  @Test
  void validateOrderStatusToAwait_WithNonAwaitingOrder_ShouldThrowException() {
    order.setStatus(OrderStatus.FAILED);

    assertThrows(OrderStatusException.class,
                 () -> OrderValidator.validateOrderStatusToAwait(order, 1000L));
  }
}

