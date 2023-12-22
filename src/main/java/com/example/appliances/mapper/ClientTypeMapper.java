package com.example.appliances.mapper;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.ClientType;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ClientTypeRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ClientTypeResponse;
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
public interface ClientTypeMapper {
    ClientTypeResponse entityToResponse(ClientType entity);


    ClientType requestToEntity(ClientTypeRequest request);


    void update(@MappingTarget ClientType entity, ClientTypeRequest request);
}
