package com.example.teamvoytest.api.dto.product;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveProductRequest {

  private List<Long> productIds;

}
