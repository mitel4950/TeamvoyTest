package com.example.teamvoytest.api.controller;


import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/api/product", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductApi {

  @GetMapping
  List<ProductDto> listProducts();

  @GetMapping("/{productId}")
  ProductDto getProductById(@PathVariable("productId") long productId);

  @PostMapping
  List<ProductDto> insertProducts(@RequestBody List<ProductDto> products);

  @DeleteMapping("/{productId}")
  void removeProduct(@PathVariable("productId") long productId);

  @PostMapping("/bulk-delete")
  void removeProduct(@RequestBody RemoveProductRequest request);

}
