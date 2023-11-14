package com.example.teamvoytest.domain.model;

import com.example.teamvoytest.domain.model.composite_key.ProductByOrderId;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_by_order")
public class ProductByOrder {

  @EmbeddedId
  private ProductByOrderId id;

  @Column(name = "count")
  private Integer count;

  @Column(name = "cost_in_cents")
  private Integer costInCents;

  public Long getOrderId() {
    return id.getOrderId();
  }

  public Long getProductId() {
    return id.getProductId();
  }
}

