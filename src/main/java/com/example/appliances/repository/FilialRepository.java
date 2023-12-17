package com.example.appliances.repository;

import com.example.appliances.entity.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    Filial findByFilCode(String filCode);
}