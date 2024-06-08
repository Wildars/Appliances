package com.example.appliances.statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    public List<ProductStatisticsResponse> getTopSellingProducts();

    public List<ManagerStatisticsResponse> getTopSellingManagers();

    Double getRevenueByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    Double getRevenueByFilial(Long filialId, LocalDateTime startDate, LocalDateTime endDate);
    Double getRevenueByCategory(Long categoryId, LocalDateTime startDate, LocalDateTime endDate);
}