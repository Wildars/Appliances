package com.example.appliances.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStatisticsResponse {
    private String productName;
    private Long totalSold;

    // Конструктор, геттеры и сеттеры
}