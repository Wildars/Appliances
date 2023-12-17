package com.example.appliances.mapper;

import com.example.appliances.entity.Filial;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.FilialResponse;
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
public interface FilialsMapper {
    FilialResponse entityToResponse(Filial entity);

    Filial requestToEntity(FilialRequest request);

    void update(@MappingTarget Filial entity, FilialRequest request);
}