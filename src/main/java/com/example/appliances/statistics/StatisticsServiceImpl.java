package com.example.appliances.statistics;

import com.example.appliances.repository.OrderRepository;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticRepository statisticsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public List<ProductStatisticsResponse> getTopSellingProducts(Long filialId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = statisticsRepository.findTopSellingProductsByFilialIdAndDateRange(filialId, startDate, endDate);

        return results.stream()
                .map(result -> {
                    String productName = (String) result[0];
                    Long totalQuantity = (Long) result[1];
                    return new ProductStatisticsResponse(productName, totalQuantity);
                })
                .limit(5) // Ограничиваем результат до топ-5 продуктов
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ManagerStatisticsResponse> getTopSellingManagers(Long filialId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = orderRepository.findTopSellingManagersByFilialIdAndDateRange(filialId, startDate, endDate);

        // Считаем общий доход
        double totalRevenue = results.stream()
                .mapToDouble(result -> (Double) result[2])
                .sum();

        return results.stream()
                .map(result -> {
                    String name = (String) result[0];
                    String surname = (String) result[1];
                    Double revenue = (Double) result[2];
                    Double revenuePercentage = (revenue / totalRevenue) * 100;
                    return new ManagerStatisticsResponse(name, surname, revenue, revenuePercentage);
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Double getRevenueByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return statisticsRepository.findTotalRevenueByPeriod(startDate, endDate);
    }

    @Override
    @Transactional
    public Double getRevenueByFilial(Long filialId, LocalDateTime startDate, LocalDateTime endDate) {
        return statisticsRepository.findTotalRevenueByFilial(filialId, startDate, endDate);
    }

    @Override
    @Transactional
    public Double getRevenueByCategory(Long categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        return statisticsRepository.findTotalRevenueByCategory(categoryId, startDate, endDate);
    }


    @Override
    @Transactional
    public StorePerformanceMetrics getStorePerformanceMetrics(Long filialId, LocalDateTime startDate, LocalDateTime endDate) {
        Double averageCheck = orderRepository.findAverageCheckByFilialIdAndDateRange(filialId, startDate, endDate);
        Long transactionCount = orderRepository.findTransactionCountByFilialIdAndDateRange(filialId, startDate, endDate);
        Long visitorCount = orderRepository.findVisitorCountByFilialIdAndDateRange(filialId, startDate, endDate);

        return StorePerformanceMetrics.builder()
                .averageCheck(averageCheck != null ? averageCheck : 0)
                .transactionCount(transactionCount != null ? transactionCount : 0)
                .visitorCount(visitorCount != null ? visitorCount : 0)
                .build();
    }

}