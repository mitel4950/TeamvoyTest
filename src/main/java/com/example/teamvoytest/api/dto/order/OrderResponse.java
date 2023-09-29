package com.example.teamvoytest.api.dto.order;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

  private Long id;
  private List<ProductForOrderDto> products;
  private OrderStatus status;
  private OffsetDateTime createdAt;

}
