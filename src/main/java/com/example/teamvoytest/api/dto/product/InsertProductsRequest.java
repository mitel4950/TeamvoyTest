package com.example.teamvoytest.api.dto.product;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertProductsRequest {

  @Valid
  @Size(min = 1, message = "Products list cannot be empty")
  private List<ProductDto> products;
}
