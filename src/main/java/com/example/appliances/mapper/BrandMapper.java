package com.example.appliances.mapper;

import com.example.appliances.entity.Brand;
import com.example.appliances.model.request.BrandRequest;
import com.example.appliances.model.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
        }
)
public interface BrandMapper {
    BrandResponse entityToResponse(Brand entity);

    Brand requestToEntity(BrandRequest request);

    void update(@MappingTarget Brand entity, BrandRequest request);
}
