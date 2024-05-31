package com.example.appliances.api;

import com.example.appliances.statistics.ManagerStatisticsResponse;
import com.example.appliances.statistics.ProductStatisticsResponse;
import com.example.appliances.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsApi {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/top-selling-products")
    public List<ProductStatisticsResponse> getTopSellingProducts() {
        return statisticsService.getTopSellingProducts();
    }

    @GetMapping("/top-selling-managers")
    public List<ManagerStatisticsResponse> getTopSellingManagers() {
        return statisticsService.getTopSellingManagers();
    }
}

