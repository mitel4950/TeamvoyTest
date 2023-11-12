package com.example.teamvoytest.controller;

import com.example.teamvoytest.api.controller.ProductApi;
import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import com.example.teamvoytest.api.service.ProductService;
import com.example.teamvoytest.validator.ProductValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

  private final ProductService service;
  private final ProductValidator validator;

  @Override
  public List<ProductDto> listProducts(boolean showBookedCount) {
    return service.listProducts(showBookedCount);
  }

  @Override
  public ProductDto getProductById(long productId) {
    return service.getProductById(productId);
  }

  @Override
  public List<ProductDto> insertProducts(InsertProductsRequest insertProductsRequest) {
    validator.validateProductsToInsert(insertProductsRequest);
    return service.insertProducts(insertProductsRequest);
  }

  @Override
  public void unavailableProduct(long productId) {
    service.unavailableProduct(productId);
  }

  @Override
  public void unavailableProducts(RemoveProductRequest request) {
    service.unavailableProducts(request);
  }
}
