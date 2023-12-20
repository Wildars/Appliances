package com.example.appliances.mapper;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Filial;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.ClientResponse;
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
public interface ClientMapper {
    ClientResponse entityToResponse(Client entity);


    Client requestToEntity(ClientRequest request);


    void update(@MappingTarget Client entity, ClientRequest request);
}