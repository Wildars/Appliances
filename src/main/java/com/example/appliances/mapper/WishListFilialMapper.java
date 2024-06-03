package com.example.appliances.mapper;

import com.example.appliances.entity.WishList;
import com.example.appliances.entity.WishListFilial;
import com.example.appliances.model.request.WishListFilialRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListFilialResponse;
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
                FilialMapper.class
        }
)
public interface WishListFilialMapper {
    WishListFilialResponse entityToResponse(WishListFilial entity);
    @Mapping(target = "filial", source = "filialId", qualifiedByName = "setFilial")
    WishListFilial requestToEntity(WishListFilialRequest request);

    void update(@MappingTarget WishListFilial entity, WishListFilialRequest request);
}