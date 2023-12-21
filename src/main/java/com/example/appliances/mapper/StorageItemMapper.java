package com.example.appliances.mapper;

import com.example.appliances.entity.Storage;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.model.response.StorageResponse;
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
                ProductMapper.class
        }
)
public interface StorageItemMapper {

    StorageItemResponse entityToResponse(StorageItem entity);

    @Mapping(target = "storage", source = "storageId", qualifiedByName = "setStorage")
    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    StorageItem requestToEntity(StorageItemRequest request);

    void update(@MappingTarget StorageItem entity, StorageItemRequest request);
}
