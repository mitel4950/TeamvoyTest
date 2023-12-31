package com.example.teamvoytest.api.controller;


import com.example.teamvoytest.api.dto.product.InsertProductsRequest;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.api.dto.product.RemoveProductRequest;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/api/product", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductApi {

  @GetMapping
  List<ProductDto> listProducts(
      @NotNull @RequestParam(defaultValue = "false") boolean showBookedCount
  );

  @GetMapping("/{productId}")
  ProductDto getProductById(@NotNull @PathVariable("productId") long productId);

  @PostMapping
  List<ProductDto> insertProducts(@NotNull @Valid @RequestBody InsertProductsRequest insertProductsRequest);

  @DeleteMapping("/{productId}")
  void unavailableProduct(@NotNull @PathVariable("productId") long productId);

  @PostMapping("/bulk-unavailable")
  void unavailableProducts(@NotNull @Valid @RequestBody RemoveProductRequest request);

}
