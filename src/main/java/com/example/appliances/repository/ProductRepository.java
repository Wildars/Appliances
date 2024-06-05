package com.example.appliances.repository;

import com.example.appliances.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> , JpaSpecificationExecutor<Product> {
    List<Product> findAllByIdIn(List<UUID> productIds);

    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    List<Product> findAllNotDeleted();

    @Query("SELECT COUNT(p) FROM Product p")
    Long countAllProducts();

//    @Query("SELECT p.name, SUM(oi.quantity) as totalSold " +
//            "FROM OrderItem oi " +
//            "JOIN oi.product p " +
//            "GROUP BY p.name " +
//            "ORDER BY totalSold DESC")
//    List<Object[]> findTopSellingProducts();

}