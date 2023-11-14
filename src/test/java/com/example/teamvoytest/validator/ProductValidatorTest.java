package com.example.teamvoytest.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.exception.InvalidDataException;
import com.example.teamvoytest.exception.RecordNotFoundException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductValidator productValidator;

  private Product product1;
  private Product product2;

  @BeforeEach
  void setUp() {
    product1 = new Product(1L, "Product 1", 1000, 10, null);
    product2 = new Product(2L, "Product 2", 2000, 20, null);
  }

  @Test
  void validateProductsToInsert_WithDuplicateIds_ShouldThrowException() {
    InsertProductsRequest request = new InsertProductsRequest(List.of(
        new ProductDto(1L, "Product 1", 1000, 10, ProductStatus.AVAILABLE, null),
        new ProductDto(1L, "Product 1", 1000, 10, ProductStatus.AVAILABLE, null)
    ));

    assertThrows(InvalidDataException.class, () -> productValidator.validateProductsToInsert(request));
  }

  @Test
  void validateProductExistence_WithNonExistingProducts_ShouldThrowException() {
    List<Long> productIds = List.of(1L, 3L);
    when(productService.getEntitiesByIds(Set.of(1L, 3L))).thenReturn(List.of(product1));

    assertThrows(InvalidDataException.class, () -> productValidator.validateProductExistence(productIds));
  }

  @Test
  void validateProductExistence_WithAllExistingProducts_ShouldNotThrowException() {
    List<Long> productIds = List.of(1L, 2L);
    when(productService.getEntitiesByIds(Set.of(1L, 2L))).thenReturn(List.of(product1, product2));

    assertDoesNotThrow(() -> productValidator.validateProductExistence(productIds));
  }

  @Test
  void validateProductsExistence_WithMismatchedProductIds_ShouldThrowException() {
    List<Product> existingProducts = List.of(product1);
    Set<Long> requestedIds = Set.of(1L, 2L);

    assertThrows(RecordNotFoundException.class, () -> productValidator.validateProductsExistence(existingProducts, requestedIds));
  }
}
