package com.example.appliances.mapper;

import com.example.appliances.entity.WishList;
import com.example.appliances.entity.WishListItem;
import com.example.appliances.model.request.WishListItemRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListItemResponse;
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
                StorageMapper.class,
                WishListMapper.class
        }
)
public interface WishListItemMapper {

    WishListItemResponse entityToResponse(WishListItem entity);

    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    @Mapping(target = "wishList", source = "wishListId", qualifiedByName = "setWishList")
    WishListItem requestToEntity(WishListItemRequest request);

    void update(@MappingTarget WishListItem entity, WishListItemRequest request);
}