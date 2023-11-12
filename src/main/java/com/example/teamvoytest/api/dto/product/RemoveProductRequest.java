package com.example.teamvoytest.api.dto.product;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveProductRequest {

  @NotNull(message = "Products list cannot be null")
  @Size(min = 1, message = "Products list cannot be empty")
  private Set<Long> productIds;

}
