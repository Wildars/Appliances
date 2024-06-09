package com.example.appliances.repository;

import com.example.appliances.entity.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule, Long> {
    boolean existsByManagerIdAndShiftStartBeforeAndShiftEndAfter(Long managerId, LocalDateTime shiftStart, LocalDateTime shiftEnd);
}