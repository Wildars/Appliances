package com.example.appliances.mapper;

import com.example.appliances.entity.Supply;
import com.example.appliances.model.request.SupplyRequest;
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
                StorageMapper.class,
        }
)
public interface SupplyMapper {

    SupplyResponse entityToResponse(Supply entity);

    @Mapping(target = "storage", source = "storageId", qualifiedByName = "setStorage")
    Supply requestToEntity(SupplyRequest request);

    void update(@MappingTarget Supply entity, SupplyRequest request);
}
