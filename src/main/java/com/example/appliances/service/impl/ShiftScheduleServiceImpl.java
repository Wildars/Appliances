package com.example.appliances.service.impl;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Manager;
import com.example.appliances.entity.ShiftSchedule;
import com.example.appliances.mapper.ShiftScheduleMapper;
import com.example.appliances.model.request.ShiftScheduleRequest;
import com.example.appliances.model.response.ShiftScheduleResponse;
import com.example.appliances.repository.FilialRepository;
import com.example.appliances.repository.ManagerRepository;
import com.example.appliances.repository.ShiftScheduleRepository;
import com.example.appliances.service.ShiftScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShiftScheduleServiceImpl implements ShiftScheduleService {

    @Autowired
    private final ShiftScheduleRepository shiftScheduleRepository;

    @Autowired
    private final ManagerRepository managerRepository;

    @Autowired
    private final FilialRepository filialRepository;

    @Autowired
    private final ShiftScheduleMapper shiftScheduleMapper;

    public ShiftScheduleServiceImpl(ShiftScheduleRepository shiftScheduleRepository, ManagerRepository managerRepository, FilialRepository filialRepository, ShiftScheduleMapper shiftScheduleMapper) {
        this.shiftScheduleRepository = shiftScheduleRepository;
        this.managerRepository = managerRepository;
        this.filialRepository = filialRepository;
        this.shiftScheduleMapper = shiftScheduleMapper;
    }

    @Override
    public ShiftScheduleResponse createShiftSchedule(ShiftScheduleRequest request) {
        // Проверяем, существует ли менеджер и филиал
        Manager manager = managerRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Filial filial = filialRepository.findById(request.getFilialId())
                .orElseThrow(() -> new RuntimeException("Filial not found"));

        // Создаем новую смену
        ShiftSchedule shiftSchedule = ShiftSchedule.builder()
                .manager(manager)
                .filial(filial)
                .shiftStart(request.getShiftStart())
                .shiftEnd(request.getShiftEnd())
                .build();

        ShiftSchedule savedShiftSchedule = shiftScheduleRepository.save(shiftSchedule);
        return shiftScheduleMapper.entityToResponse(savedShiftSchedule);
    }

    @Override
    public boolean isManagerWorking(Long managerId, LocalDateTime dateTime) {
        // Проверяем, есть ли смена у менеджера в указанное время
        return shiftScheduleRepository.existsByManagerIdAndShiftStartBeforeAndShiftEndAfter(managerId, dateTime, dateTime);
    }
}

