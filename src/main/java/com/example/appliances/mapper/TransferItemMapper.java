package com.example.appliances.mapper;

import com.example.appliances.entity.Supply;
import com.example.appliances.entity.TransferItem;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.TransferItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                ProductMapper.class,
                TransferItemMapper.class
        }
)
public interface TransferItemMapper {
    TransferItemResponse entityToResponse(TransferItem entity);

}
