package com.example.appliances.statistics;

import com.example.appliances.repository.OrderRepository;
import com.example.appliances.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public List<ProductStatisticsResponse> getTopSellingProducts() {
        return null;
    }
//        List<Object[]> results = productRepository.findTopSellingProducts();
//        return results.stream()
//                .map(result -> new ProductStatisticsResponse((String) result[0], (Long) result[1]))
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public List<ManagerStatisticsResponse> getTopSellingManagers() {
        List<Object[]> results = orderRepository.findTopSellingManagers();

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


}