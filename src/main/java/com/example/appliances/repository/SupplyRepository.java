package com.example.appliances.repository;

import com.example.appliances.entity.Supplier;
import com.example.appliances.entity.Supply;
import com.example.appliances.enums.SupplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRepository extends JpaRepository<Supply,Long> {
    Page<Supply> findByStorageIdAndStatus(Long aLong, SupplyStatus supplyStatus, Pageable paging);

    Page<Supply> findByStorageId(Long aLong, Pageable paging);

    Page<Supply> findByStatus(SupplyStatus supplyStatus, Pageable paging);

    Page<Supply> findBySupplierId(Long aLong, Pageable paging);
}
