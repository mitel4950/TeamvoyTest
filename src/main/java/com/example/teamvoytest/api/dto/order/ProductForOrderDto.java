package com.example.teamvoytest.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForOrderDto {

  private Long id;
  private String name;
  private Integer costInCents;
  private Integer count;

}
