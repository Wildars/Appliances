package com.example.appliances.service;

import com.example.appliances.entity.Supply;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SupplyService {
//     SupplyResponse create(SupplyRequest supplyRequest);

     SupplyResponse findById(Long id);

     public SupplyResponse createFromWishList(long wishListId, String supplierUsername, String supplierPassword);

     SupplyResponse update(SupplyRequest supplyRequest, Long id);

     List<SupplyItemResponse> findAll();

     public List<WishListResponse> getAllWishListItems();
     public Page<WishListResponse> getAllWishListItemsPaged(int page, int size, Optional<Boolean> sortOrder, String sortBy);
     public List<SupplyResponse> findAlls();

     public Page<SupplyResponse> getAllSuppliers(int page,
                                                 int size,
                                                 Optional<Boolean> sortOrder,
                                                 String sortBy);

     void deleteById(Long id);

     public SupplyResponse create(SupplyRequest supplyRequest, String supplierPin, String supplierPassword);
}
