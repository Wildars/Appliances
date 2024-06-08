package com.example.appliances.mapper;

import com.example.appliances.entity.Order;
import com.example.appliances.model.request.OrderRequest;
import com.example.appliances.model.request.OrderRequestDelivery;
import com.example.appliances.model.response.OrderResponse;
import com.example.appliances.model.response.SimpleOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                OrderItemMapper.class,
                UserMapper.class,
                ManagerMapper.class
        }
)
public interface OrderMapper {
    OrderResponse entityToResponse(Order entity);

    SimpleOrderResponse entityToResponseSimple(Order entity);

    @Mapping(target = "manager", source = "managerId", qualifiedByName = "setManager")
    Order requestToEntity(OrderRequest request);

    @Mapping(target = "manager", source = "managerId", qualifiedByName = "setManager")
    Order requestToEntityDelivery(OrderRequestDelivery request);

    void update(@MappingTarget Order entity, OrderRequest request);
}
