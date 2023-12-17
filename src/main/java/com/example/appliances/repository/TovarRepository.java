package com.example.appliances.repository;

import com.example.appliances.entity.Tovar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TovarRepository extends JpaRepository<Tovar, Long> {
}