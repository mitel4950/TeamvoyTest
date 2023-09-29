package com.example.teamvoytest.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForCreateOrderRequest {

  private Long orderId;
  private Integer count;

}
