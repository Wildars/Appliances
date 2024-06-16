package com.example.appliances.repository;

import com.example.appliances.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Order,Long> {


    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.schedule BETWEEN :startDate AND :endDate")
    Double findTotalRevenueByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(o.totalAmount) FROM Order o JOIN o.orderItems oi JOIN oi.filialItem fi WHERE fi.filial.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate")
    Double findTotalRevenueByFilial(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(o.totalAmount) FROM Order o JOIN o.orderItems oi JOIN oi.filialItem fi JOIN fi.product p JOIN p.categories c WHERE c.id = :categoryId AND o.schedule BETWEEN :startDate AND :endDate")
    Double findTotalRevenueByCategory(@Param("categoryId") Long categoryId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT p.name, SUM(oi.quantity) as totalQuantity " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "JOIN oi.filialItem fi " +
            "JOIN fi.product p " +
            "JOIN fi.filial f " +
            "WHERE f.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsByFilialIdAndDateRange(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
