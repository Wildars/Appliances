package com.example.appliances.repository;

import com.example.appliances.entity.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage,Long> {
    @Query("SELECT DISTINCT s.storageCode FROM Storage s")
    List<String> findAllStorageCodes();

    Page<Storage> findById(Long aLong, PageRequest of);
}
