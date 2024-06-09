package com.example.appliances.api;

import com.example.appliances.model.request.ShiftScheduleRequest;
import com.example.appliances.model.response.ShiftScheduleResponse;
import com.example.appliances.service.ShiftScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shifts")
public class ShiftScheduleApi {

    @Autowired
    private ShiftScheduleService shiftScheduleService;

    @PostMapping
    public ShiftScheduleResponse createShiftSchedule(@RequestBody ShiftScheduleRequest request) {
        return shiftScheduleService.createShiftSchedule(request);
    }

    @GetMapping("/is-working")
    public boolean isManagerWorking(@RequestParam Long managerId, @RequestParam LocalDateTime dateTime) {
        return shiftScheduleService.isManagerWorking(managerId, dateTime);
    }
}
