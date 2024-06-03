package com.example.appliances.mapper;

import com.example.appliances.entity.Storage;
import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.SaleResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.StorageResponseBot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                StorageItemMapper.class
        }
)
public interface StorageMapper {

    StorageResponse entityToResponse(Storage entity);

    StorageResponseBot entityToResponseBot(Storage entity);


    Storage requestToEntity(StorageRequest request);

    void update(@MappingTarget Storage entity, StorageRequest request);
}
