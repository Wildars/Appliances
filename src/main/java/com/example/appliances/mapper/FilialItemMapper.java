package com.example.appliances.mapper;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.StorageItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                FilialMapper.class,
                ProductMapper.class
        }
)
public interface FilialItemMapper {

    FilialItemResponse entityToResponse(FilialItem entity);

    @Mapping(target = "filial", source = "filialId", qualifiedByName = "setFilial")
    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    FilialItem requestToEntity(FilialItemRequest request);

    void update(@MappingTarget FilialItem entity, FilialItemRequest request);
}
