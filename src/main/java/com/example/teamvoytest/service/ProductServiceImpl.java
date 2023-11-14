package com.example.teamvoytest.service;

import static com.example.teamvoytest.exception.ErrorMessages.PRODUCT_NOT_FOUND;

import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.ProductStatus;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import com.example.teamvoytest.api.service.ProductByOrderService;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.domain.mapper.ProductMapper;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import com.example.teamvoytest.domain.repository.ProductRepository;
import com.example.teamvoytest.exception.RecordNotFoundException;
import com.example.teamvoytest.validator.ProductValidator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;
  private final ProductMapper mapper;
  private final ProductByOrderService productByOrderService;
  private final ProductValidator productValidator;

  @Override
  public List<ProductDto> listProducts(boolean showBookedCount) {
    List<Product> all = repository.findAll();
    List<ProductDto> dtos = mapper.toDtoList(all);
    return showBookedCount ? fillProductBookedCountValues(dtos) : dtos;
  }

  @Override
  public ProductDto getProductById(long productId) {
    Product product = getEntityById(productId);
    return mapper.toDto(product);
  }

  @Override
  public List<ProductDto> insertProducts(InsertProductsRequest insertProductsRequest) {
    List<Product> entities = mapper.toEntityList(insertProductsRequest.getProducts());
    List<Product> saved = repository.saveAll(entities);
    return mapper.toDtoList(saved);
  }

  @Override
  public void unavailableProduct(long productId) {
    Product product = getEntityById(productId);
    product.setStatus(ProductStatus.UNAVAILABLE);
    repository.save(product);
  }

  @Override
  public void unavailableProducts(RemoveProductRequest request) {
    List<Product> existing = getEntitiesByIds(request.getProductIds());
    existing.forEach(product -> product.setStatus(ProductStatus.UNAVAILABLE));
    repository.saveAll(existing);
  }

  @Override
  public List<Product> getEntitiesByIds(Set<Long> productIds) {
    List<Product> allById = repository.findAllById(productIds);
    productValidator.validateProductsExistence(allById, productIds);
    return allById;
  }

  @Override
  public Map<Long, Product> getEntityMapByProductByOrders(List<ProductByOrder> allByOrderId) {
    Set<Long> productIds =
        allByOrderId.stream().map(ProductByOrder::getProductId).collect(Collectors.toSet());

    List<Product> productsByIds = getEntitiesByIds(productIds);
    return productsByIds.stream().collect(Collectors.toMap(Product::getId, p -> p));
  }

  @Override
  public void minusProductInventoryCount(Long orderId) {
    List<ProductByOrder> pboList = productByOrderService.listEntitiesByOrderId(orderId);

    Set<Long> productIds =
        pboList.stream().map(ProductByOrder::getProductId).collect(Collectors.toSet());
    List<Product> products = repository.findAllById(productIds);
    Map<Long, Product> productById = products.stream()
        .collect(Collectors.toMap(Product::getId, p -> p));

    pboList.forEach(pbo -> {
      Product product = productById.get(pbo.getProductId());
      product.setCostInCents(product.getInventoryCount() - pbo.getCount());
    });
    repository.saveAll(productById.values());
  }

  private Product getEntityById(Long productId) {
    return repository.findById(productId)
        .orElseThrow(() -> new RecordNotFoundException(PRODUCT_NOT_FOUND.formatted(productId)));
  }

  private List<ProductDto> fillProductBookedCountValues(List<ProductDto> products) {
    Set<Long> productIds = products.stream()
        .map(ProductDto::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    List<ProductByOrder> activeProductByOrders =
        productByOrderService.listActiveEntitiesByProductIds(productIds);

    Map<Long, Integer> productCountMap = activeProductByOrders.stream()
        .collect(Collectors.groupingBy(ProductByOrder::getProductId,
                                       Collectors.summingInt(ProductByOrder::getCount)));

    products.forEach(p -> p.setBookedCount(productCountMap.getOrDefault(p.getId(), 0)));
    return products;
  }

}
