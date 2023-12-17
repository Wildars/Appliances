package com.example.appliances.repository;

import com.example.appliances.entity.TovarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TovarCategoryRepository extends JpaRepository<TovarCategory, Long> {
}