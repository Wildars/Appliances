package com.example.appliances.repository;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}