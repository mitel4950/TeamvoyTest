package com.example.teamvoytest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.mapper.OrderMapper;
import com.example.teamvoytest.domain.model.Order;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.model.composite_key.ProductByOrderId;
import com.example.teamvoytest.domain.repository.OrderRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  private OrderRepository repository;
  @Mock
  private OrderMapper mapper;
  @Mock
  private ProductService productService;
  @Mock
  private ProductByOrderService productByOrderService;

  @InjectMocks
  private OrderServiceImpl orderService;

  private Product product1;
  private ProductByOrder pbo1;
  private ProductByOrder pbo2;
  private ProductForOrderRequest pfor1;
  private ProductForOrderRequest pfor2;
  private Order order1;


  @BeforeEach
  void setUp() {
    product1 = new Product(1L, "Iphone 15", 1500000, 50, ProductStatus.AVAILABLE);
    pbo1 = new ProductByOrder(new ProductByOrderId(1L, 1L), 40, 1500000);
    pbo2 = new ProductByOrder(new ProductByOrderId(1L, 2L), 5, 1500000);
    pfor1 = new ProductForOrderRequest(1L, 40, 1500000);
    pfor2 = new ProductForOrderRequest(1L, 5, 1500000);
    order1 = new Order(1L, OrderStatus.AWAITING,
                       OffsetDateTime.now().toLocalDateTime().atOffset(ZoneOffset.UTC));

    ReflectionTestUtils.setField(orderService, "orderActivityTimeMilliseconds", 1000L);
  }


  @Test
  void listOrders_ShouldReturnOrdersWithOrWithoutProducts() {
    PageRequest pageable = PageRequest.of(0, 10);
    List<Order> orders = List.of(new Order(), new Order());
    Page<Order> page = new PageImpl<>(orders, pageable, orders.size());
    List<OrderResponse> orderResponses = List.of(new OrderResponse(), new OrderResponse());
    orderResponses.get(0).setId(1L);
    orderResponses.get(1).setId(2L);
    List<ProductByOrder> productByOrders = List.of(pbo1, pbo2);

    when(repository.findAll(pageable)).thenReturn(page);
    when(mapper.toResponseList(page)).thenReturn(orderResponses);
    when(productByOrderService.listEntitiesByOrderIds(Set.of(1L, 2L))).thenReturn(productByOrders);

    Page<OrderResponse> result = orderService.listOrders(true, pageable);

    assertEquals(orderResponses, result.getContent());
    assertEquals(2, result.getTotalElements());
  }

  @Test
  void getOrderById_WhenOrderExists_ShouldReturnOrder() {
    long orderId = 1L;
    Order order = new Order();
    OrderResponse orderResponse = new OrderResponse();

    when(repository.findById(orderId)).thenReturn(Optional.of(order));
    when(mapper.toResponse(order)).thenReturn(orderResponse);

    OrderResponse result = orderService.getOrderById(orderId, false);

    assertEquals(orderResponse, result);
  }

  @Test
  void createOrder_ShouldCreateAndReturnOrder() {
    CreateOrderRequest orderRequest = new CreateOrderRequest(List.of(pfor1, pfor2));
    Order createdOrder = new Order();
    OrderResponse orderResponse = new OrderResponse();
    HashSet<Long> productIds = new HashSet<>();
    List<Product> products = new ArrayList<>();

    products.add(product1);
    productIds.add(1L);

    when(repository.save(any(Order.class))).thenReturn(createdOrder);
    when(mapper.toResponse(createdOrder)).thenReturn(orderResponse);
    when(productService.getEntitiesByIds(productIds)).thenReturn(products);

    OrderResponse result = orderService.createOrder(orderRequest);

    assertEquals(orderResponse, result);
  }

  @Test
  void cancelOrder_WhenOrderExists_ShouldCancelOrder() {
    when(repository.findById(1L)).thenReturn(Optional.of(order1));

    orderService.cancelOrder(1L);

    assertEquals(OrderStatus.FAILED, order1.getStatus());
    verify(repository).save(order1);
  }

  @Test
  void confirmPayment_WhenOrderExists_ShouldCompleteOrder() {
    when(repository.findById(1L)).thenReturn(Optional.of(order1));
    doNothing().when(productService).minusProductInventoryCount(1L);

    orderService.confirmPayment(1L);

    assertEquals(OrderStatus.COMPLETED, order1.getStatus());
    verify(repository).save(order1);
  }
}

