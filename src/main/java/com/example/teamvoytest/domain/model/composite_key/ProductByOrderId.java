package com.example.teamvoytest.domain.model.composite_key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProductByOrderId implements Serializable {

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "product_id")
  private Long productId;
}
