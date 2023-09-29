package com.example.teamvoytest.api.service;

import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import java.util.List;

public interface ProductService {

  List<ProductDto> listProducts();

  ProductDto getProductById(long productId);

  List<ProductDto> insertProducts(List<ProductDto> products);

  void removeProduct(long productId);

  void removeProduct(RemoveProductRequest request);

}
