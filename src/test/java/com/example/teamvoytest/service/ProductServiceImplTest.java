package com.example.teamvoytest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.domain.mapper.ProductMapper;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.model.composite_key.ProductByOrderId;
import com.example.teamvoytest.domain.repository.ProductRepository;
import com.example.teamvoytest.exception.RecordNotFoundException;
import com.example.teamvoytest.validator.ProductValidator;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock
  private ProductRepository repository;
  @Mock
  private ProductMapper mapper;
  @Mock
  private ProductByOrderService productByOrderService;
  @Mock
  private ProductValidator productValidator;
  @InjectMocks
  private ProductServiceImpl productService;

  @Test
  void listProducts_ShouldReturnProductsWithCorrectBookedCount() {
    List<Product> productList = List.of(
        new Product(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE),
        new Product(2L, "Product 2", 200, 20, ProductStatus.AVAILABLE)
    );
    List<ProductDto> productDtoList = List.of(
        new ProductDto(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE, 5),
        new ProductDto(2L, "Product 2", 200, 20, ProductStatus.AVAILABLE, 15)
    );
    when(repository.findAll()).thenReturn(productList);
    when(mapper.toDtoList(productList)).thenReturn(productDtoList);
    List<ProductDto> result = productService.listProducts(true);
    assertEquals(productDtoList, result);
  }

  @Test
  void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
    long productId = 1L;
    when(repository.findById(productId)).thenReturn(Optional.empty());
    assertThrows(RecordNotFoundException.class, () -> productService.getProductById(productId));
  }

  @Test
  void insertProducts_ShouldReturnSavedProductDtos() {
    InsertProductsRequest insertProductsRequest = new InsertProductsRequest(List.of(
        new ProductDto(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE, null)
    ));
    List<Product> products = List.of(
        new Product(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE));
    when(mapper.toEntityList(insertProductsRequest.getProducts())).thenReturn(products);
    when(repository.saveAll(products)).thenReturn(products);
    when(mapper.toDtoList(products)).thenReturn(insertProductsRequest.getProducts());
    List<ProductDto> result = productService.insertProducts(insertProductsRequest);
    assertEquals(insertProductsRequest.getProducts(), result);
  }

  @Test
  void unavailableProduct_ShouldSetProductStatusToUnavailable() {
    Product product = new Product(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE);
    when(repository.findById(1L)).thenReturn(Optional.of(product));
    productService.unavailableProduct(1L);
    verify(repository).save(argThat(p -> p.getStatus() == ProductStatus.UNAVAILABLE));
  }

  @Test
  void unavailableProducts_ShouldSetAllProductsToUnavailable() {
    Set<Long> productIds = Set.of(1L, 2L);
    List<Product> products = List.of(
        new Product(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE),
        new Product(2L, "Product 2", 200, 20, ProductStatus.AVAILABLE)
    );
    when(repository.findAllById(productIds)).thenReturn(products);
    doNothing().when(productValidator).validateProductsExistence(any(), any());
    productService.unavailableProducts(new RemoveProductRequest(productIds));
    verify(repository).saveAll(
        argThat(ps -> ((List<Product>) ps).stream()
            .allMatch(p -> p.getStatus() == ProductStatus.UNAVAILABLE)));
  }

  @Test
  void getEntityMapByProductByOrders_ReturnsCorrectMap() {
    ProductByOrder pbo1 = new ProductByOrder(new ProductByOrderId(1L, 1L), 40, 1500000);
    ProductByOrder pbo2 = new ProductByOrder(new ProductByOrderId(1L, 2L), 5, 1500000);
    Product product1 = new Product(1L, "Product 1", 100, 10, ProductStatus.AVAILABLE);
    Product product2 = new Product(2L, "Product 2", 200, 20, ProductStatus.AVAILABLE);
    List<Product> productList = List.of(product1, product2);
    when(repository.findAllById(anySet())).thenReturn(productList);
    doNothing().when(productValidator).validateProductsExistence(any(), any());
    Map<Long, Product> result = productService.getEntityMapByProductByOrders(List.of(pbo1, pbo2));
    verify(repository).findAllById(new HashSet<>(Set.of(1L, 2L)));
    assertEquals(2, result.size());
    assertTrue(result.containsKey(1L) && result.containsKey(2L));
    assertEquals(product1, result.get(1L));
    assertEquals(product2, result.get(2L));
  }

  @Test
  void minusProductInventoryCount_ShouldDecreaseInventoryByCount() {
    Long orderId = 1L;
    ProductByOrder pbo1 = new ProductByOrder(new ProductByOrderId(orderId, 1L), 2, 1000);
    ProductByOrder pbo2 = new ProductByOrder(new ProductByOrderId(orderId, 2L), 3, 2000);
    List<ProductByOrder> pboList = List.of(pbo1, pbo2);
    Product product1 = new Product(1L, "Product 1", 1000, 10, ProductStatus.AVAILABLE);
    Product product2 = new Product(2L, "Product 2", 2000, 20, ProductStatus.AVAILABLE);
    List<Product> products = List.of(product1, product2);
    when(productByOrderService.listEntitiesByOrderId(orderId)).thenReturn(pboList);
    when(repository.findAllById(Set.of(1L, 2L))).thenReturn(products);
    productService.minusProductInventoryCount(orderId);
    assertEquals(10, product1.getInventoryCount());
    assertEquals(20, product2.getInventoryCount());
    verify(repository).saveAll(
        argThat(savedProducts -> products.containsAll((Collection<?>) savedProducts)));
  }
}
