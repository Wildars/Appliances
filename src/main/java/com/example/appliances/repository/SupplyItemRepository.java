package com.example.appliances.repository;

import com.example.appliances.entity.SupplyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem,Long>{
    @Query("SELECT si FROM SupplyItem si WHERE si.supply.supplier.pin = :pin")
    List<SupplyItem> findAllBySupplierPin(@Param("pin") String pin);

}
