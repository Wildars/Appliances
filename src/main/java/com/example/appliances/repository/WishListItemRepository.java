package com.example.appliances.repository;

import com.example.appliances.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem,Long> {
}
