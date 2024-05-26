package com.example.appliances.mapper;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.FieldResponse;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                ProductCategoryMapper.class,
                FieldMapper.class
        }
)
public interface ProductCategoryMapper {
    @Mapping(target = "parent", source = "parent.id")
    default ProductCategoryResponse entityToResponse(ProductCategory entity) {
        if (entity == null) {
            return null;
        }

        ProductCategoryResponse response = new ProductCategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setParent(entity.getParent() != null ? entity.getParent().getId() : null); // Устанавливаем parentId
        // Маппинг дочерних категорий
        List<ProductCategoryResponse> childrenResponses = entitiesToResponses(entity.getChildren());
        response.setChildren(childrenResponses);

        return response;
    }
    default List<ProductCategoryResponse> entitiesToResponses(List<ProductCategory> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(entity -> {
                    ProductCategoryResponse response = entityToResponse(entity);
                    // Маппинг полей для каждой категории
                    List<FieldResponse> fieldResponses = Optional.ofNullable(entity.getFields())
                            .orElse(Collections.emptyList()) // Если fields == null, то возвращаем пустой список
                            .stream()
                            .map(fieldMapper::entityToResponse) // Предположим, что fieldMapper доступен
                            .collect(Collectors.toList());
                    response.setFields(fieldResponses);
                    return response;
                })
                .collect(Collectors.toList());
    }
    @Mapping(target = "fields", ignore = true)
    ProductCategory requestToEntity(ProductCategoryRequest request);
    @Mapping(target = "fields", ignore = true)
    void update(@MappingTarget ProductCategory entity, ProductCategoryRequest request);

    FieldMapper fieldMapper = Mappers.getMapper(FieldMapper.class); // Создание экземпляра FieldMapper

    default List<FieldResponse> getFields(ProductCategory category) {
        if (category == null || category.getFields() == null) {
            return Collections.emptyList();
        }
        return category.getFields().stream()
                .map(fieldMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}