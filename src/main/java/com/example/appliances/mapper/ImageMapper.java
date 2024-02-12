package com.example.appliances.mapper;

import com.example.appliances.entity.Image;
import com.example.appliances.model.request.ImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(target = "name", source = "imageName")
    @Mapping(target = "image", source = "imageData", qualifiedByName = "wtf")
    @Mapping(target = "type", source = "imageExtension")
    Image toEntity(ImageDto imageDto);

    ImageDto toDTO(Image image);

    @Named("wtf")
    static byte[] wtf(String dtoDate){
        String base64String = dtoDate.split(",")[1];
        byte[] data = Base64.getDecoder().decode(base64String);
        return data;
    }
}
