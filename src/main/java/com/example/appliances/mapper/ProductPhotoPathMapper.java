package com.example.appliances.mapper;

import com.example.appliances.entity.ProductPhotoPath;
import com.example.appliances.model.request.ProductPhotoPathRequest;
import com.example.appliances.model.response.ProductPhotoPathResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
        }
)
public interface ProductPhotoPathMapper {
//    @Mapping(source = "photoPaths", target = "photoPaths")
    ProductPhotoPathResponse entityToResponse(ProductPhotoPath entity);

    @Mapping(target = "id", ignore = true) // Игнорируем id, так как его значение будет присвоено автоматически
    ProductPhotoPath requestToEntity(ProductPhotoPathRequest request);
}
