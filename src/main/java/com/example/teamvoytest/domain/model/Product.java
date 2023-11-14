package com.example.teamvoytest.domain.model;

import com.example.teamvoytest.api.dto.product.ProductStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "cost_in_cents")
  private Integer costInCents;

  @Column(name = "inventory_count")
  private Integer inventoryCount;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

}
