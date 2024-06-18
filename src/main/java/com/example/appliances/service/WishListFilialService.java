package com.example.appliances.service;

import com.example.appliances.model.request.WishListFilialRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListFilialResponse;
import com.example.appliances.model.response.WishListResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface WishListFilialService {

    public WishListFilialResponse create(WishListFilialRequest request);

    public WishListFilialResponse findById(Long id) ;

    public Page<WishListFilialResponse> getAllPage(int page,
                                                   int size,
                                                   Optional<Boolean> sortOrder,
                                                   String sortBy,
                                                   Optional<Long> filialId);

    public WishListFilialResponse update(WishListFilialRequest request, Long id);

    public List<WishListFilialResponse> findAll();

    public void deleteById(Long id) ;
    public void returnWishList(Long orderId);
}
