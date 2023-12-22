package com.example.appliances.mapper;

import com.example.appliances.entity.DiscountCategory;
import com.example.appliances.entity.Filial;
import com.example.appliances.model.request.DiscountCategoryRequest;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.DiscountCategoryResponse;
import com.example.appliances.model.response.FilialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class

        }
)
public interface DiscountCategoryMapper {

    DiscountCategoryResponse entityToResponse(DiscountCategory entity);


    DiscountCategory requestToEntity(DiscountCategoryRequest request);


    void update(@MappingTarget DiscountCategory entity, DiscountCategoryRequest request);
}
