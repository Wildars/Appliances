package com.example.appliances.mapper;

import com.example.appliances.entity.Product;
import com.example.appliances.model.request.PermissionRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.PermissionResponse;
import com.example.appliances.model.response.ProductResponse;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                BrandMapper.class,
                ProducingCountryMapper.class,
                ProductCategoryMapper.class,
                ProductPhotoPathMapper.class
        }
)
public interface ProductMapper {

    ProductResponse entityToResponse(Product entity);
    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "setProductCategories")
    @Mapping(target = "brand", source = "brandId", qualifiedByName = "setBrand")
    @Mapping(target = "producingCountry", source = "producingCountryId", qualifiedByName = "setProducingCountry")
//    @Mapping(target = "status", source = "statusId", qualifiedByName = "setStatus")
    Product requestToEntity(ProductRequest request, @Context List<MultipartFile> photos);

    List<ProductResponse> toResponseList(List<Product> productList);
    void update(@MappingTarget Product entity, ProductRequest request);
}