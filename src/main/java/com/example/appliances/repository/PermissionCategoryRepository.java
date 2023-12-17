package com.example.appliances.repository;

import com.example.appliances.entity.PermissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionCategoryRepository extends JpaRepository<PermissionCategory, Short> {
}