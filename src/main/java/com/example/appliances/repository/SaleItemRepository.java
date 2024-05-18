package com.example.appliances.repository;


import com.example.appliances.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findAllBySaleStatusId(Long saleStatusId);

    @Query("SELECT s.numberNakladnoy FROM SaleItem s WHERE s.numberNakladnoy LIKE CONCAT(:datePrefix, '%')")
    List<String> findNakladnoyNumbersByDate(String datePrefix);
}