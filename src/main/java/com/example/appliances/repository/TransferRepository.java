package com.example.appliances.repository;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Transfer;
import com.example.appliances.enums.WishListStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByToFilial(Filial toFilial);
}
