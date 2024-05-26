package com.example.appliances.mapper;

import com.example.appliances.entity.Field;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.model.request.FieldRequest;
import com.example.appliances.model.response.FieldResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                ProductCategory.class,
                DefaultMapper.class
        }
)
public interface FieldMapper {
    FieldResponse entityToResponse(Field entity);


    @Mapping(target = "category", source = "categoryId", qualifiedByName = "setProductCategory")
    Field requestToEntity(FieldRequest request);

    void update(@MappingTarget Field entity, FieldRequest request);
}