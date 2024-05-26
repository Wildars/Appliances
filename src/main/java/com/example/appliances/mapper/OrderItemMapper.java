package com.example.appliances.mapper;

import com.example.appliances.entity.OrderItem;
import com.example.appliances.model.request.OrderItemRequest;
import com.example.appliances.model.response.OrderItemResponse;
import com.example.appliances.model.response.SimpleOrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                OrderMapper.class,
                ProductMapper.class
        }
)
public interface OrderItemMapper {
    OrderItemResponse entityToResponse(OrderItem entity);

    SimpleOrderItemResponse entityToResponseSimple(OrderItem entity);

    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    @Mapping(target = "order", source = "orderId", qualifiedByName = "setOrder")
    OrderItem requestToEntity(OrderItemRequest request);

    void update(@MappingTarget OrderItem entity, OrderItemRequest request);
}
