package com.example.appliances.repository;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilialItemRepository extends JpaRepository<FilialItem,Long> {

    Optional<FilialItem> findByFilialIdAndProductId(Long filialId, UUID productId);

    Optional<FilialItem> findById(Long id);

    FilialItem findByProductId(UUID productId);

    Optional<FilialItem> findByProductIdAndFilialId(UUID productId, Long filialId);


    @Query("SELECT fi FROM FilialItem fi WHERE fi.filial.id = :filialId")
    List<FilialItem> findByFilialId(@Param("filialId") Long filialId);
//    Optional<FilialItem> findByProductIdAndStorageId(UUID productId, Long storageId);
}
