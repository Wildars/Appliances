package com.example.appliances.repository;

import com.example.appliances.entity.ReturnFilialItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnFilialItem,Long> {
}
