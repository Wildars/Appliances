package com.example.appliances.repository;

import com.example.appliances.entity.ProductField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductField, Long> {
}
