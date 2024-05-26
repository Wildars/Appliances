package com.example.appliances.mapper;


import com.example.appliances.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DefaultMapper {


    @Named("setBrand")
    default Brand setBrand(Long id) {
        if (id == null)
            return null;
        else
            return Brand.builder().id(id).build();
    }
    @Named("setField")
    default Field setField(Long id) {
        if (id == null)
            return null;
        else
            return Field.builder().id(id).build();
    }

    @Named("setProducingCountry")
    default ProducingCountry setProducingCountry(Long id) {
        if (id == null)
            return null;
        else
            return ProducingCountry.builder().id(id).build();
    }


    @Named("setProductCategories")
    default List<ProductCategory> setProductCategories(List<Long> ids) {
        if (ids == null)
            return null;
        if (ids.isEmpty())
            return new ArrayList<>();

        List<ProductCategory> result = new ArrayList<>();
        for (var i : ids)
            result.add(ProductCategory.builder().id(i).build());
        return result;
    }

    @Named("setProductCategory")
    default ProductCategory setProductCategory(Long id) {
        if (id == null)
            return null;
        else
            return ProductCategory.builder().id(id).build();
    }

    @Named("setStatus")
    default Status setStatus(Long id) {
        if (id == null)
            return null;
        else
            return Status.builder().id(id).build();
    }

    @Named("setProduct")
    default Product setProduct(UUID id) {
        if (id == null)
            return null;
        else
            return Product.builder().id(id).build();
    }
    @Named("setOrder")
    default Order setOrder(Long id) {
        if (id == null)
            return null;
        else
            return Order.builder().id(id).build();
    }


    @Named("setProducts")
    default List<Product> setProducts(List<UUID> ids) {
        if (ids == null)
            return null;
        if (ids.isEmpty())
            return new ArrayList<>();

        List<Product> result = new ArrayList<>();
        for (var i : ids)
            result.add(Product.builder().id(i).build());
        return result;
    }

    @Named("setUser")
    default User setUser(Long id) {
        if (id == null)
            return null;
        else
            return User.builder().id(id).build();
    }

    @Named("setFilial")
    default Filial setFilial(Long id) {
        if (id == null)
            return null;
        else
            return Filial.builder().id(id).build();
    }

    @Named("setDiscountCategory")
    default DiscountCategory setDiscountCategory(Long id) {
        if (id == null)
            return null;
        else
            return DiscountCategory.builder().id(id).build();
    }

    @Named("setClientType")
    default ClientType setClientType(Long id) {
        if (id == null)
            return null;
        else
            return ClientType.builder().id(id).build();
    }

    @Named("setSupply")
    default Supply setSupply(Long id) {
        if (id == null)
            return null;
        else
            return Supply.builder().id(id).build();
    }

    @Named("setStorage")
    default Storage setStorage(Long id) {
        if (id == null)
            return null;
        else
            return Storage.builder().id(id).build();
    }

//    @Named("setSale")
//    default Sale setSale(Long id) {
//        if (id == null)
//            return null;
//        else
//            return Sale.builder().id(id).build();
//    }

    @Named("setImage")
    default Image setImage(Long id) {
        if (id == null)
            return null;
        else
            return Image.builder().id(id).build();
    }
//    @Named("setProduct")
//    default Product setProduct(Long id) {
//        if (id == null)
//            return null;
//        else
//            return Product.builder().id(id).build();
//    }

    @Named("setOrganizations")
    default List<Filial> setOrganizations(List<Long> ids) {
        if (ids == null)
            return null;
        if (ids.isEmpty())
            return new ArrayList<>();

        List<Filial> result = new ArrayList<>();
        for (var i : ids)
            result.add(Filial.builder().id(i).build());
        return result;
    }

    @Named("setPermissions")
    default List<Permission> setPermissions(List<Short> ids) {
        if (ids == null)
            return null;
        if (ids.isEmpty())
            return new ArrayList<>();

        List<Permission> result = new ArrayList<>();
        for (var i : ids)
            result.add(Permission.builder().id(i).build());
        return result;
    }

    @Named("setRoles")
    default List<Role> setRoles(List<Long> ids) {
        if (ids == null)
            return null;
        if (ids.isEmpty())
            return new ArrayList<>();

        List<Role> result = new ArrayList<>();
        for (var i : ids)
            result.add(Role.builder().id(i).build());
        return result;
    }
    default LocalDateTime mapToLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }
}
