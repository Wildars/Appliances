package com.example.appliances.mapper;

import com.example.appliances.entity.Supplier;
import com.example.appliances.entity.Supply;
import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.SupplyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
        }
)
public interface SupplierMapper {

    SupplierResponse entityToResponse(Supplier entity);

    Supplier requestToEntity(SupplierRequest request);

    void update(@MappingTarget Supplier entity, SupplierRequest request);
}
