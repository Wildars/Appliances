package com.example.appliances.mapper;

import com.example.appliances.entity.Field;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductField;
import com.example.appliances.model.request.ProductFieldRequest;
import com.example.appliances.model.response.ProductFieldResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                Field.class,
                Product.class,
                DefaultMapper.class,
                ProductCategoryMapper.class
        }
)
public interface ProductFieldMapper {
    ProductFieldResponse entityToResponse(ProductField entity);

    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    @Mapping(target = "field", source = "fieldId", qualifiedByName = "setField")
    ProductField requestToEntity(ProductFieldRequest request);

    void update(@MappingTarget ProductField entity, ProductFieldRequest request);
}
