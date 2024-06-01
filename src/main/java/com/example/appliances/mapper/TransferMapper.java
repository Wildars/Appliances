package com.example.appliances.mapper;

import com.example.appliances.entity.Supply;
import com.example.appliances.entity.Transfer;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.TransferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                StorageMapper.class,
//                ProductMapper.class,
                FilialMapper.class
        }
)
public interface TransferMapper {
    TransferResponse entityToResponse(Transfer entity);

}

