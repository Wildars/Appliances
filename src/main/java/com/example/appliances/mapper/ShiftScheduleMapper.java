package com.example.appliances.mapper;

import com.example.appliances.entity.Role;
import com.example.appliances.entity.ShiftSchedule;
import com.example.appliances.model.request.RoleRequest;
import com.example.appliances.model.request.ShiftScheduleRequest;
import com.example.appliances.model.response.RoleResponse;
import com.example.appliances.model.response.ShiftScheduleResponse;
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
                ManagerMapper.class
        }
)
public interface ShiftScheduleMapper {
    ShiftScheduleResponse entityToResponse(ShiftSchedule entity);

    @Mapping(target = "manager", source = "managerId", qualifiedByName = "setManager")
    @Mapping(target = "filial", source = "filialId", qualifiedByName = "setFilial")
    ShiftSchedule requestToEntity(ShiftScheduleRequest request);

    void update(@MappingTarget ShiftSchedule entity, ShiftScheduleRequest request);
}