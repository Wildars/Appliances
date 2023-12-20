package com.example.appliances.repository;

import com.example.appliances.entity.Sale;
import com.example.appliances.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}