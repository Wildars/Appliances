package com.example.appliances.repository;

import com.example.appliances.entity.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageItemRepository extends JpaRepository<StorageItem,Long> {

    Optional<StorageItem> findByStorageIdAndProductId(Long storageId, UUID productId);

    StorageItem findByProductId(UUID productId);

    Optional<StorageItem> findByProductIdAndStorageId(UUID productId, Long storageId);


}
