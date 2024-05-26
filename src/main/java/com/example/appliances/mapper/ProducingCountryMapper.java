package com.example.appliances.mapper;

import com.example.appliances.entity.ProducingCountry;
import com.example.appliances.model.request.ProducingCountryRequest;
import com.example.appliances.model.response.ProducingCountryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
        }
)
public interface ProducingCountryMapper {
    ProducingCountryResponse entityToResponse(ProducingCountry entity);

    ProducingCountry requestToEntity(ProducingCountryRequest request);

    void update(@MappingTarget ProducingCountry entity, ProducingCountryRequest request);
}
