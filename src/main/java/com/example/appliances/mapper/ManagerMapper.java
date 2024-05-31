package com.example.appliances.mapper;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Manager;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ManagerRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ManagerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
        }
)
public interface ManagerMapper {

    ManagerResponse entityToResponse(Manager entity);


    Manager requestToEntity(ManagerRequest request);

    void update(@MappingTarget Manager entity, ManagerRequest request);

}