package com.example.appliances.mapper;

import com.example.appliances.entity.User;
import com.example.appliances.model.request.UserRequest;
import com.example.appliances.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                DefaultMapper.class,
                FilialMapper.class,
                RoleMapper.class
        }
)
public interface UserMapper {
    UserResponse entityToResponse(User entity);

    @Mapping(target = "filials", source = "filialIds", qualifiedByName = "setOrganizations")
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "setRoles")
    User requestToEntity(UserRequest request);

    @Mapping(target = "filials", source = "filialIds", qualifiedByName = "setOrganizations")
    @Mapping(target = "roles", source = "roleIds", qualifiedByName = "setRoles")
    void update(@MappingTarget User entity, UserRequest request);
}