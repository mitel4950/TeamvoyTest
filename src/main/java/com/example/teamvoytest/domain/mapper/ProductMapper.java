package com.example.teamvoytest.domain.mapper;

import com.example.teamvoytest.api.dto.order.ProductForOrderDto;
import com.example.teamvoytest.api.dto.product.ProductDto;
import com.example.teamvoytest.domain.model.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDto toDto(Product entity);

  Product toEntity(ProductDto dto);

  List<ProductDto> toDtoList(List<Product> entities);

  List<Product> toEntityList(List<ProductDto> dtos);

  @Mapping(target = "count", source = "count")
  ProductForOrderDto toProductForOrderDto(Product entity, Integer count);

}
