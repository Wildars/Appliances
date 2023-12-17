package com.example.appliances.mapper;


import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Permission;
import com.example.appliances.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DefaultMapper {




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
