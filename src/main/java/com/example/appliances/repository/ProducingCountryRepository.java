package com.example.appliances.repository;

import com.example.appliances.entity.ProducingCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducingCountryRepository extends JpaRepository<ProducingCountry,Long> {
}
