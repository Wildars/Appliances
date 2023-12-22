package com.example.appliances.mapper;

import com.example.appliances.entity.Supply;
import com.example.appliances.entity.SupplyItem;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                SupplyMapper.class,
                ProductMapper.class
        }
)
public interface SupplyItemMapper {

    SupplyItemResponse entityToResponse(SupplyItem entity);
    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    @Mapping(target = "supply", source = "supplyId", qualifiedByName = "setSupply")
    SupplyItem requestToEntity(SupplyItemRequest request);

    void update(@MappingTarget SupplyItem entity, SupplyItemRequest request);
}
