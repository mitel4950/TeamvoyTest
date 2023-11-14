package com.example.teamvoytest.api.dto.order;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @NotNull(message = "Products list cannot be null")
  @Size(min = 1, message = "Products list cannot be empty")
  private List<ProductForOrderRequest> products;

}
