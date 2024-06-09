package com.example.appliances.service;

import com.example.appliances.model.request.ShiftScheduleRequest;
import com.example.appliances.model.response.ShiftScheduleResponse;

import java.time.LocalDateTime;

public interface ShiftScheduleService {
    ShiftScheduleResponse createShiftSchedule(ShiftScheduleRequest request);
    boolean isManagerWorking(Long managerId, LocalDateTime dateTime);
}
