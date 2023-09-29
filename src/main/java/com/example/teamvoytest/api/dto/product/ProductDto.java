package com.example.teamvoytest.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private Long id;
  private String name;
  private Integer costInCents;
  private Integer inventoryCount;
  private ProductStatus status;

}
