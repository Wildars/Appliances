package com.example.appliances.repository;

import com.example.appliances.entity.Prodajy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdajyRepository extends JpaRepository<Prodajy, Long> {
}