package com.example.appliances.statistics;

import java.util.List;

public interface StatisticsService {

    public List<ProductStatisticsResponse> getTopSellingProducts();

    public List<ManagerStatisticsResponse> getTopSellingManagers();
}
