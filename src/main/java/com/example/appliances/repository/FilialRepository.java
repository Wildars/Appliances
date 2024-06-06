package com.example.appliances.repository;

import com.example.appliances.entity.Filial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    Filial findByFilCode(String filCode);

    @Query("SELECT DISTINCT f.filCode FROM Filial f")
    List<String> findAllFilCodes();

    Page<Filial> findById(Long id, Pageable pageable);
    Page<Filial> findByFilCode(String filCode, Pageable pageable);
    Page<Filial> findByIdAndFilCode(Long id, String filCode, Pageable pageable);
}