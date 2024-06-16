package com.example.appliances.statistics;

public interface InventoryService {
    InventoryStatisticsResponse getInventoryStatistics(Long storageId);
}
