package com.example.appliances.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerStatisticsResponse {
    private String name;
    private String surname;
    private Double totalRevenue;
    private Double revenuePercentage;

}
