package com.example.appliances.repository;

import com.example.appliances.entity.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    Filial findByFilCode(String filCode);

    @Query("SELECT DISTINCT f.filCode FROM Filial f")
    List<String> findAllFilCodes();
}