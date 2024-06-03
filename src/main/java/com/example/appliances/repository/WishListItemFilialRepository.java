package com.example.appliances.repository;

import com.example.appliances.entity.WishListItemFilial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListItemFilialRepository extends JpaRepository<WishListItemFilial,Long> {
}
