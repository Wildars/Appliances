package com.example.appliances.repository;

import com.example.appliances.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Short> {
}