package com.example.appliances.model.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftScheduleRequest {
    Long managerId;
    Long filialId;
    LocalDateTime shiftStart;
    LocalDateTime shiftEnd;
}
