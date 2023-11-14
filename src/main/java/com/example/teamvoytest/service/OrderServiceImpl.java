package com.example.teamvoytest.service;

import static com.example.teamvoytest.exception.ErrorMessages.ORDER_NOT_FOUND;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderResponse;
import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderDto;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.api.service.OrderService;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.mapper.OrderMapper;
import com.example.teamvoytest.domain.mapper.ProductMapper;
import com.example.teamvoytest.domain.model.Order;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.repository.OrderRepository;
import com.example.teamvoytest.exception.RecordNotFoundException;
import com.example.teamvoytest.validator.OrderValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository repository;
  private final OrderMapper mapper;
  private final ProductMapper productMapper;
  private final ProductService productService;
  private final ProductByOrderService productByOrderService;

  @Value("${custom.order-activity-time-milliseconds}")
  private Long orderActivityTimeMilliseconds;

  @Override
  public Page<OrderResponse> listOrders(boolean includeProducts, PageRequest pageable) {
    Page<Order> allOrders = repository.findAll(pageable);
    List<OrderResponse> orderResponses = mapper.toResponseList(allOrders);

    if (includeProducts) {
      orderResponses = fillOrdersWithProducts(orderResponses);
    }

    return new PageImpl<>(orderResponses, pageable, allOrders.getTotalElements());
  }

  @Override
  public OrderResponse getOrderById(long orderId, boolean includeProduct) {
    Order order = getEntityById(orderId);
    OrderResponse orderResponse = mapper.toResponse(order);

    return includeProduct ? fillOrderWithProducts(orderResponse) : orderResponse;
  }

  @Override
  public OrderResponse createOrder(CreateOrderRequest orderRequest) {
    Order newOrder = new Order(OrderStatus.AWAITING);
    Order createdOrder = repository.save(newOrder);

    List<ProductForOrderRequest> products = fillRequestsWithCost(orderRequest.getProducts());
    productByOrderService.saveAll(products, createdOrder.getId());

    OrderResponse orderResponse = mapper.toResponse(createdOrder);
    return fillOrderWithProducts(orderResponse);
  }

  @Override
  public void cancelOrder(long orderId) {
    Order order = getEntityById(orderId);
    OrderValidator.validateOrderStatusToCancel(order);

    order.setStatus(OrderStatus.FAILED);
    repository.save(order);
  }

  @Override
  public void confirmPayment(long orderId) {
    Order order = getEntityById(orderId);
    OrderValidator.validateOrderStatusToAwait(order, orderActivityTimeMilliseconds);

    productService.minusProductInventoryCount(order.getId());

    order.setStatus(OrderStatus.COMPLETED);
    repository.save(order);
  }


  private Order getEntityById(long orderId) {
    return repository.findById(orderId)
        .orElseThrow(() -> new RecordNotFoundException(ORDER_NOT_FOUND.formatted(orderId)));
  }

  private OrderResponse fillOrderWithProducts(OrderResponse orderResponse) {
    List<ProductByOrder> pboList = productByOrderService.listEntitiesByOrderId(
        orderResponse.getId());
    Map<Long, Product> productsByIds = productService.getEntityMapByProductByOrders(pboList);

    List<ProductForOrderDto> productsForOrderDto = pboList.stream()
        .map(pbo -> mapToProductForOrderDto(productsByIds, pbo))
        .toList();

    orderResponse.setProducts(productsForOrderDto);
    return orderResponse;
  }

  private ProductForOrderDto mapToProductForOrderDto(Map<Long, Product> productMap,
                                                     ProductByOrder productByOrder) {
    Product product = productMap.get(productByOrder.getProductId());
    return productMapper.toProductForOrderDto(product, productByOrder.getCount());
  }

  private List<OrderResponse> fillOrdersWithProducts(List<OrderResponse> orders) {
    orders.forEach(o -> o.setProducts(new ArrayList<>()));
    Map<Long, OrderResponse> orderResponseMap = orders.stream()
        .collect(Collectors.toMap(OrderResponse::getId, o -> o));

    List<ProductByOrder> pboList =
        productByOrderService.listEntitiesByOrderIds(orderResponseMap.keySet());
    Map<Long, Product> productsByIds =
        productService.getEntityMapByProductByOrders(pboList);

    for (ProductByOrder productByOrder : pboList) {
      OrderResponse orderResponse = orderResponseMap.get(productByOrder.getOrderId());
      Product product = productsByIds.get(productByOrder.getProductId());

      ProductForOrderDto productForOrderDto =
          productMapper.toProductForOrderDto(product, productByOrder.getCount());

      orderResponse.getProducts().add(productForOrderDto);
    }

    return new ArrayList<>(orderResponseMap.values());
  }

  private List<ProductForOrderRequest> fillRequestsWithCost(List<ProductForOrderRequest> pboList) {
    Set<Long> productIds = pboList.stream()
        .map(ProductForOrderRequest::getProductId)
        .collect(Collectors.toSet());

    Map<Long, Product> productsByIds = productService.getEntitiesByIds(productIds).stream()
        .collect(Collectors.toMap(Product::getId, p -> p));

    return pboList.stream()
        .map(request -> updateProductForOrderRequestWithCost(productsByIds, request))
        .toList();
  }

  private ProductForOrderRequest updateProductForOrderRequestWithCost(Map<Long, Product> productsById,
                                                                      ProductForOrderRequest request) {
    Product product = productsById.get(request.getProductId());
    request.setCostInCents(product.getCostInCents());
    return request;
  }
}
