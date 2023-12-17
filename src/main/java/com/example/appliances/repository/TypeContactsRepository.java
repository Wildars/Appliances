package com.example.appliances.repository;

import com.example.appliances.entity.TypeContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeContactsRepository extends JpaRepository<TypeContacts, Long> {
}