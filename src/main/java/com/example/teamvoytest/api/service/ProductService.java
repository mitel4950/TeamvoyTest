package com.example.teamvoytest.api.service;

import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import com.example.teamvoytest.domain.model.Product;
import com.example.teamvoytest.domain.model.ProductByOrder;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {

  List<ProductDto> listProducts(boolean showBookedCount);

  ProductDto getProductById(long productId);

  List<ProductDto> insertProducts(InsertProductsRequest insertProductsRequest);

  void unavailableProduct(long productId);

  void unavailableProducts(RemoveProductRequest request);

  List<Product> getEntitiesByIds(Set<Long> productIds);

  Map<Long, Product> getEntityMapByProductByOrders(List<ProductByOrder> allByOrderId);

  void minusProductInventoryCount(Long orderId);
}
