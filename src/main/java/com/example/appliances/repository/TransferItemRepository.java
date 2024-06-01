package com.example.appliances.repository;

import com.example.appliances.entity.Transfer;
import com.example.appliances.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Long> {
}
