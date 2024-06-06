package com.example.appliances.mapper;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.User;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.request.UserRequest;
import com.example.appliances.model.response.FilialResponse;
import com.example.appliances.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                FilialItemMapper.class

        }
)
public interface FilialMapper {
    FilialResponse entityToResponse(Filial entity);


    Filial requestToEntity(FilialRequest request);


    void update(@MappingTarget Filial entity, FilialRequest request);
}