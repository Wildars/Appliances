package com.example.appliances.repository;

import com.example.appliances.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByIdIn(List<UUID> productIds);

    @Query("SELECT COUNT(p) FROM Product p")
    Long countAllProducts();

}