package com.example.teamvoytest.validator;

import static com.example.teamvoytest.exception.ErrorMessages.INSUFFICIENT_AMOUNT_OF_PRODUCTS;
import static com.example.teamvoytest.exception.ErrorMessages.ORDER_IS_COMPLETED;
import static com.example.teamvoytest.exception.ErrorMessages.ORDER_IS_FAILED;
import static com.example.teamvoytest.exception.ErrorMessages.PRODUCTS_ARE_NOT_AVAILABLE;
import static com.example.teamvoytest.validator.CommonValidationUtils.validateUniqueIds;

import com.example.teamvoytest.api.dto.order.CreateOrderRequest;
import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.model.Order;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.exception.InabilityToLinkOrderException;
import com.example.teamvoytest.exception.OrderStatusException;
import com.example.teamvoytest.exception.ProductStatusException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

  private final ProductService productService;
  private final ProductByOrderService productByOrderService;

  private final ProductValidator productValidator;


  public void validateCreateOrderRequest(CreateOrderRequest createOrdersRequest) {
    List<Long> productIds = createOrdersRequest.getProducts().stream()
        .map(ProductForOrderRequest::getProductId)
        .collect(Collectors.toList());

    validateUniqueIds(productIds);

    List<Product> products = productService.getEntitiesByIds(new HashSet<>(productIds));
    productValidator.validateProductExistence(productIds);
    validateProductsStatuses(products);

    List<ProductByOrder> pboList =
        productByOrderService.listActiveEntitiesByProductIds(new HashSet<>(productIds));

    validateProductAvailability(createOrdersRequest.getProducts(), products, pboList);
  }

  private static void validateProductsStatuses(List<Product> products) {
    List<Product> unavailableStatusProducts = products.stream()
        .filter(p -> p.getStatus() != ProductStatus.AVAILABLE || p.getInventoryCount() <= 0)
        .collect(Collectors.toList());

    if (!unavailableStatusProducts.isEmpty()) {
      String productIdsInString = unavailableStatusProducts.stream()
          .map(Product::getId)
          .map(String::valueOf)
          .collect(Collectors.joining(", "));
      throw new ProductStatusException(
          String.format(PRODUCTS_ARE_NOT_AVAILABLE, productIdsInString));
    }


  }

  public static void validateProductAvailability(List<ProductForOrderRequest> productsInRequest,
                                                 List<Product> products,
                                                 List<ProductByOrder> pboList) {
    Map<Long, Integer> inventoryCountByProductId = products.stream()
        .collect(Collectors.toMap(Product::getId, Product::getInventoryCount));

    pboList.forEach(pbo -> inventoryCountByProductId.merge(
        pbo.getProductId(),
        pbo.getCount(),
        (inventoryLeft, minusCount) -> inventoryLeft - minusCount));

    String productIdsInString = productsInRequest.stream()
        .filter(p -> inventoryCountByProductId.getOrDefault(p.getProductId(), 0) < p.getCount())
        .map(p -> String.valueOf(p.getProductId()))
        .collect(Collectors.joining(", "));

    if (!productIdsInString.isEmpty()) {
      throw new InabilityToLinkOrderException(
          String.format(INSUFFICIENT_AMOUNT_OF_PRODUCTS, productIdsInString));
    }
  }

  public static void validateOrderStatusToCancel(Order order) {
    if (order.getStatus() == OrderStatus.COMPLETED) {
      throw new OrderStatusException(String.format(ORDER_IS_COMPLETED, order.getId()));
    }
  }

  public static void validateOrderStatusToAwait(Order order, Long orderActivityTimeMilliseconds) {
    OffsetDateTime expirationTime =
        order.getCreatedAt().plus(Duration.ofMillis(orderActivityTimeMilliseconds));

    if (order.getStatus() != OrderStatus.AWAITING || OffsetDateTime.now().isAfter(expirationTime)) {
      throw new OrderStatusException(String.format(ORDER_IS_FAILED, order.getId()));
    }
  }
}
