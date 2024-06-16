package com.example.appliances.statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    public List<ManagerStatisticsResponse> getTopSellingManagers(Long filialId, LocalDateTime startDate, LocalDateTime endDate);

    public List<ProductStatisticsResponse> getTopSellingProducts(Long filialId, LocalDateTime startDate, LocalDateTime endDate);

    Double getRevenueByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    Double getRevenueByFilial(Long filialId, LocalDateTime startDate, LocalDateTime endDate);
    Double getRevenueByCategory(Long categoryId, LocalDateTime startDate, LocalDateTime endDate);
    public StorePerformanceMetrics getStorePerformanceMetrics(Long filialId, LocalDateTime startDate, LocalDateTime endDate);
}