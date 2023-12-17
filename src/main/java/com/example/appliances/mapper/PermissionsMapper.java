package com.example.appliances.mapper;

import com.example.appliances.entity.Permission;
import com.example.appliances.model.request.PermissionRequest;
import com.example.appliances.model.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
        }
)
public interface PermissionsMapper {
    PermissionResponse entityToResponse(Permission entity);

    Permission requestToEntity(PermissionRequest request);

    void update(@MappingTarget Permission entity, PermissionRequest request);
}