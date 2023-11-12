package com.example.teamvoytest.domain.mapper;

import com.example.teamvoytest.api.dto.order.OrderResponse;
import com.example.teamvoytest.domain.model.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  OrderResponse toResponse(Order entity);

  List<OrderResponse> toResponseList(Page<Order> dtos);

}
