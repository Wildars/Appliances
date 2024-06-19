package com.example.appliances.mapper;

import com.example.appliances.entity.Role;
import com.example.appliances.model.request.RoleRequest;
import com.example.appliances.model.response.RoleResponse;
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
public interface RoleMapper {
    RoleResponse entityToResponse(Role entity);

    Role requestToEntity(RoleRequest request);

    void update(@MappingTarget Role entity, RoleRequest request);
}