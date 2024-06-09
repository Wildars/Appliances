package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftScheduleResponse {
    ManagerResponse manager;
    FilialResponse filial;
    LocalDateTime shiftStart;
    LocalDateTime shiftEnd;
}
