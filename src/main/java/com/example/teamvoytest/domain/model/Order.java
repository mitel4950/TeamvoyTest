package com.example.teamvoytest.domain.model;

import com.example.teamvoytest.api.dto.order.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "created_at", updatable = false, nullable = false)
  private OffsetDateTime createdAt;


  public Order(OrderStatus status) {
    this.status = status;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = OffsetDateTime.now().toLocalDateTime().atOffset(ZoneOffset.UTC);
  }
}
