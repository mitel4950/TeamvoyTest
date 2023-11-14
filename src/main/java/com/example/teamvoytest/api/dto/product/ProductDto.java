package com.example.teamvoytest.api.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  @PositiveOrZero(message = "Product ID must not be negative")
  private Long id;

  @NotBlank(message = "Product name must not be empty")
  private String name;

  @NotNull(message = "Cost cannot be null")
  @Min(value = 0, message = "Cost in cents must not be negative")
  private Integer costInCents;

  @NotNull(message = "Inventory count cannot be null")
  @Min(value = 0, message = "Cost in cents must not be negative")
  private Integer inventoryCount;

  @NotNull(message = "Status count cannot be null")
  private ProductStatus status;

  private Integer bookedCount;

  @JsonProperty
  public Integer getBookedCount() {
    return bookedCount;
  }

  @JsonIgnore
  public void setBookedCount(Integer bookedCount) {
    this.bookedCount = bookedCount;
  }
}
