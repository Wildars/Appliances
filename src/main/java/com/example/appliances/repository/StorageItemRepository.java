package com.example.appliances.repository;

import com.example.appliances.entity.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageItemRepository extends JpaRepository<StorageItem,Long> {

    Optional<StorageItem> findByStorageIdAndProductId(Long storageId, Long productId);

    StorageItem findByProductId(Long productId);

    Optional<StorageItem> findByProductIdAndStorageId(Long productId, Long storageId);


}
