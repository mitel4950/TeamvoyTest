package com.example.teamvoytest.domain.model.composite_key;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
