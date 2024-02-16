package com.example.appliances.mapper;

import com.example.appliances.entity.ProductCategory;
import com.example.appliances.entity.WishList;
import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.WishListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                StorageMapper.class
        }
)
public interface WishListMapper {
    WishListResponse entityToResponse(WishList entity);
    @Mapping(target = "storage", source = "storageId", qualifiedByName = "setStorage")
    WishList requestToEntity(WishListRequest request);

    void update(@MappingTarget WishList entity, WishListRequest request);
}