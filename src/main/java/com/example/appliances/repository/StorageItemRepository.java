package com.example.appliances.repository;

import com.example.appliances.entity.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageItemRepository extends JpaRepository<StorageItem,Long> {

    Optional<StorageItem> findByStorageIdAndProductId(Long storageId, UUID productId);

    StorageItem findByProductId(UUID productId);

    Optional<StorageItem> findByProductIdAndStorageId(UUID productId, Long storageId);


    @Query("SELECT SUM(si.quantity) FROM StorageItem si WHERE si.storage.id = :storageId")
    Long findTotalInventoryByStorageId(@Param("storageId") Long storageId);

    @Query("SELECT pc.name, SUM(si.quantity) " +
            "FROM StorageItem si " +
            "JOIN si.product p " +
            "JOIN p.categories pc " +
            "WHERE si.storage.id = :storageId " +
            "GROUP BY pc.name")
    List<Object[]> findInventoryByCategoryAndStorageId(@Param("storageId") Long storageId);
}