package com.example.appliances.mapper;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
        }
)
public interface ProductCategoryMapper {
    ProductCategoryResponse entityToResponse(ProductCategory entity);

    ProductCategory requestToEntity(ProductCategoryRequest request);

    void update(@MappingTarget ProductCategory entity, ProductCategoryRequest request);
}