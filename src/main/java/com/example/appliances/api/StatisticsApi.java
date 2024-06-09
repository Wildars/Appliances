package com.example.appliances.api;

import com.example.appliances.repository.StatisticRepository;
import com.example.appliances.statistics.ManagerStatisticsResponse;
import com.example.appliances.statistics.ProductStatisticsResponse;
import com.example.appliances.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsApi {



    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/top-selling-products")
    public List<ProductStatisticsResponse> getTopSellingProducts(@RequestParam Long filialId) {
        return statisticsService.getTopSellingProducts(filialId);
    }

    @GetMapping("/top-selling-managers")
    public List<ManagerStatisticsResponse> getTopSellingManagers(@RequestParam Long filialId) {
        return statisticsService.getTopSellingManagers(filialId);
    }


    @GetMapping("/revenue-by-period")
    public ResponseEntity<Double> getRevenueByPeriod(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = statisticsService.getRevenueByPeriod(startDate, endDate);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/revenue-by-filial")
    public ResponseEntity<Double> getRevenueByFilial(@RequestParam(required = false) Long filialId,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = statisticsService.getRevenueByFilial(filialId, startDate, endDate);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/revenue-by-category")
    public ResponseEntity<Double> getRevenueByCategory(@RequestParam(required = false) Long categoryId,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = statisticsService.getRevenueByCategory(categoryId, startDate, endDate);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }
}

