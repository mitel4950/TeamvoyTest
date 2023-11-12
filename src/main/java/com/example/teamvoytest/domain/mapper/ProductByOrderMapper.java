package com.example.teamvoytest.domain.mapper;

import com.example.teamvoytest.api.dto.order.ProductForOrderRequest;
import com.example.teamvoytest.domain.model.ProductByOrder;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductByOrderMapper {


  @Mapping(target = "id.orderId", source = "orderId")
  @Mapping(target = "id.productId", source = "request.productId")
  @Mapping(target = "count", source = "request.count")
  ProductByOrder requestToEntity(ProductForOrderRequest request, Long orderId);

  default ArrayList<ProductByOrder> dtoListToEntityList(List<ProductForOrderRequest> requests,
                                                        Long orderId) {
    if (requests == null && orderId == null) {
      return new ArrayList<>();
    }

    ArrayList<ProductByOrder> arrayList = new ArrayList<>();

    for (ProductForOrderRequest p : requests) {
      ProductByOrder productByOrder = requestToEntity(p, orderId);
      arrayList.add(productByOrder);
    }
    return arrayList;
  }

}
