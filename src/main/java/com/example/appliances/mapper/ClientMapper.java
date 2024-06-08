package com.example.appliances.mapper;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.ClientType;
import com.example.appliances.entity.Filial;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.FilialResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                ClientTypeMapper.class,
                DiscountCategoryMapper.class,
                GenderMapper.class,

        }
)
public interface ClientMapper {
    ClientResponse entityToResponse(Client entity);

    @Mapping(target = "clientType", source = "clientTypeId", qualifiedByName = "setClientType")
    @Mapping(target = "gender", source = "genderId", qualifiedByName = "setGender")
    @Mapping(target = "discountCategory", source = "discountCategoryId", qualifiedByName = "setDiscountCategory")
    Client requestToEntity(ClientRequest request);

//    @Mapping(target = "clientType", source = "clientTypeId", qualifiedByName = "setClientTypeForResponse")
//    Client responseToEntity(ClientResponse response);

    void update(@MappingTarget Client entity, ClientRequest request);


//    @Named("setClientTypeForResponse")
//    default ClientType setClientTypeForResponse(Long id) {
//        if (id == null)
//            return null;
//        else
//            return ClientType.builder().id(id).build();
//    }
}