package com.example.appliances.repository;

import com.example.appliances.entity.ReturnFilial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepository extends JpaRepository<ReturnFilial, Long> {
}
