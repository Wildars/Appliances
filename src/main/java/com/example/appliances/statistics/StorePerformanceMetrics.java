package com.example.appliances.statistics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePerformanceMetrics {
    private Double averageCheck;
    private Long transactionCount;
    private Long visitorCount;
}
