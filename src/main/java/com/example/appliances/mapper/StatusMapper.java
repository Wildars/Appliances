package com.example.appliances.mapper;

import com.example.appliances.entity.Status;
import com.example.appliances.model.request.StatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);
    Status toEntity(StatusDto statusDto);
    StatusDto toDTO(Status status);
    List<StatusDto> toDTOList(List<Status> statusList);
}
