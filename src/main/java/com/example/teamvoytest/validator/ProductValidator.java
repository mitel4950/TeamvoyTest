package com.example.teamvoytest.validator;

import static com.example.teamvoytest.exception.ErrorMessages.PRODUCTS_NOT_FOUND;
import static com.example.teamvoytest.exception.ErrorMessages.PRODUCT_IDS_ARE_NOT_FOUND;
import static com.example.teamvoytest.validator.CommonValidationUtils.validateUniqueIds;

import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.exception.InvalidDataException;
import com.example.teamvoytest.exception.RecordNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

  private final ProductService service;

  public void validateProductsToInsert(InsertProductsRequest insertProductsRequest) {
    List<Long> ids = insertProductsRequest.getProducts().stream()
        .map(ProductDto::getId)
        .filter(Objects::nonNull)
        .toList();

    validateUniqueIds(ids);
    validateProductExistence(ids);
  }

  public void validateProductExistence(List<Long> productIdsInRequest) {
    List<Product> productsByIds = service.getEntitiesByIds(new HashSet<>(productIdsInRequest));
    validateProductExistence(productIdsInRequest, productsByIds);
  }

  public void validateProductExistence(List<Long> productIdsInRequest,
                                       List<Product> existProducts) {
    if (existProducts.size() != productIdsInRequest.size()) {
      List<Long> foundIds = existProducts.stream().map(Product::getId).toList();

      String missingIdsString = productIdsInRequest.stream()
          .filter(id -> !foundIds.contains(id))
          .map(String::valueOf)
          .collect(Collectors.joining(", "));

      throw new InvalidDataException(PRODUCT_IDS_ARE_NOT_FOUND.formatted(missingIdsString));
    }
  }

  public void validateProductsExistence(List<Product> existing, Set<Long> requestedIds) {
    if (requestedIds.size() != existing.size()) {
      List<Long> existIds = existing.stream().map(Product::getId).toList();
      String notExistingIds = requestedIds.stream()
          .filter(existIds::contains)
          .map(String::valueOf)
          .collect(Collectors.joining(", "));

      throw new RecordNotFoundException(PRODUCTS_NOT_FOUND.formatted(notExistingIds));
    }
  }
}
