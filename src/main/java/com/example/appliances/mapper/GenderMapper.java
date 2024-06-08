package com.example.appliances.mapper;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.Gender;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.GenderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {

        }
)
public interface GenderMapper {

    GenderResponse entityToResponse(Gender entity);


}
