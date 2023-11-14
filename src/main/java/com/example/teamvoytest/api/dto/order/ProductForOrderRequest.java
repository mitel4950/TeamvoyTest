package com.example.teamvoytest.api.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductForOrderRequest {

  @NotNull(message = "Product ID cannot be null")
  @Min(value = 0, message = "Product ID cannot be negative")
  private Long productId;

  @NotNull(message = "Count cannot be null")
  @Min(value = 0, message = "Count must not be negative")
  private Integer count;

  private Integer costInCents;

  @JsonProperty
  public Integer getCostInCents() {
    return costInCents;
  }

  @JsonIgnore
  public void setCostInCents(Integer costInCents) {
    this.costInCents = costInCents;
  }
}
