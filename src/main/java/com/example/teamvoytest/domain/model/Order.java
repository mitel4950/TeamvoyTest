package com.example.teamvoytest.domain.model;

import com.example.teamvoytest.api.dto.order.OrderStatus;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
