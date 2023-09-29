package com.example.teamvoytest.api.dto.order;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  private List<ProductForCreateOrderRequest> products;

}
