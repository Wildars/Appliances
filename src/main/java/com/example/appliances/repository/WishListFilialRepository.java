package com.example.appliances.repository;

import com.example.appliances.entity.WishListFilial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListFilialRepository extends JpaRepository<WishListFilial,Long> {
    Page<WishListFilial> findByFilialId(Long aLong, PageRequest of);

    Optional<WishListFilial> findByFilialId(Long filialId);
}
