//package com.example.appliances.mapper;
//
//import com.example.appliances.entity.ProductCategory;
//import com.example.appliances.entity.Sale;
//import com.example.appliances.model.request.ProductCategoryRequest;
//import com.example.appliances.model.request.SaleRequest;
//import com.example.appliances.model.response.ProductCategoryResponse;
//import com.example.appliances.model.response.SaleResponse;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//@Mapper(
//        componentModel = "spring",
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
//        uses = {
//                DefaultMapper.class,
//                UserMapper.class,
//                FilialMapper.class
//        }
//)
//public interface SaleMapper {
//    SaleResponse entityToResponse(Sale entity);
//
//    @Mapping(target = "user", source = "userId", qualifiedByName = "setUser")
//    @Mapping(target = "filial", source = "filialId", qualifiedByName = "setFilial")
//    Sale requestToEntity(SaleRequest request);
//
//    void update(@MappingTarget Sale entity, SaleRequest request);
//}