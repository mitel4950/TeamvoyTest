package com.example.teamvoytest.domain.repository;

import com.example.teamvoytest.api.dto.order.OrderStatus;
import com.example.teamvoytest.domain.model.ProductByOrder;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductByOrderRepository extends JpaRepository<ProductByOrder, Long> {

  List<ProductByOrder> findAllById_OrderIdIn(Set<Long> orderIds);
  List<ProductByOrder> findAllById_OrderId(Long orderId);

  @Query("SELECT pbo FROM ProductByOrder pbo " +
      "JOIN Order o ON pbo.id.orderId = o.id " +
      "WHERE pbo.id.productId IN :productIds " +
      "AND o.createdAt >= :cutoff " +
      "AND o.status = :status")
  List<ProductByOrder> findRecentOrdersWithProductIds(Set<Long> productIds,
                                                      OffsetDateTime cutoff,
                                                      OrderStatus status);
}
