package com.example.appliances.service;

import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.WishListResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface WishListService {

    public WishListResponse create(WishListRequest wishListRequest);

    public WishListResponse findById(Long id) ;

    public Page<WishListResponse> getAllByPage(int page,
                                               int size,
                                               Optional<Boolean> sortOrder,
                                               String sortBy,
                                               Optional<Long> storageId);

    public WishListResponse update(WishListRequest wishListRequest, Long id);

    public List<WishListResponse> findAll();

    public void deleteById(Long id) ;
}
