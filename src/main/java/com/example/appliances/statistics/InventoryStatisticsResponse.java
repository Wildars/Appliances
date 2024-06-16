package com.example.appliances.statistics;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryStatisticsResponse {
    private Long totalInventory;
    private List<CategoryInventory> categoryInventories;
}