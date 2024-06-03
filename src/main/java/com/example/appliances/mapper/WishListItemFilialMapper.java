package com.example.appliances.mapper;

import com.example.appliances.entity.WishListFilial;
import com.example.appliances.entity.WishListItem;
import com.example.appliances.entity.WishListItemFilial;
import com.example.appliances.model.request.WishListItemFilialRequest;
import com.example.appliances.model.request.WishListItemRequest;
import com.example.appliances.model.response.WishListItemFilialResponse;
import com.example.appliances.model.response.WishListItemResponse;
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
                WishListFilialMapper.class
        }
)
public interface WishListItemFilialMapper {

    WishListItemFilialResponse entityToResponse(WishListItemFilial entity);

    @Mapping(target = "product", source = "productId", qualifiedByName = "setProduct")
    @Mapping(target = "wishListFilial", source = "wishListFilialId", qualifiedByName = "setWishListFilial")
    WishListItemFilial requestToEntity(WishListItemFilialRequest request);

    void update(@MappingTarget WishListItemFilial entity, WishListItemFilialRequest request);
}