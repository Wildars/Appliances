package com.example.appliances.service.impl;

import com.example.appliances.entity.WishList;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.WishListRepository;
import com.example.appliances.service.ProductCategoryService;
import com.example.appliances.service.WishListService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WishListServiceImpl implements WishListService {
    WishListRepository wishListRepository;
    WishListMapper wishListMapper;

    @Autowired
    public WishListServiceImpl(WishListRepository wishListRepository, WishListMapper wishListMapper) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
    }
    @Override
    public Page<WishListResponse> getAllProductCategory(int page,
                                                        int size,
                                                        Optional<Boolean> sortOrder,
                                                        String sortBy) {
        Pageable paging = null;

        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<WishList> saleItemsPage = wishListRepository.findAll(paging);

        return saleItemsPage.map(wishListMapper::entityToResponse);
    }

    @Override
    @Transactional
    public WishListResponse create(WishListRequest wishListRequest) {
        WishList product = wishListMapper.requestToEntity(wishListRequest);
        WishList savedProduct = wishListRepository.save(product);
        return wishListMapper.entityToResponse(savedProduct);
    }
    @Override
    @Transactional
    public WishListResponse findById(Long id) {
        WishList product = wishListRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует!"));
        return wishListMapper.entityToResponse(product);
    }
    @Override
    @Transactional
    public WishListResponse update(WishListRequest wishListRequest, Long productId) {
        WishList product = wishListRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует"));
        wishListMapper.update(product, wishListRequest);
        WishList updatedProduct = wishListRepository.save(product);
        return wishListMapper.entityToResponse(updatedProduct);
    }
    @Override
    @Transactional
    public List<WishListResponse> findAll() {
        List<WishList> products = wishListRepository.findAll();
        return products.stream().map(wishListMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        wishListRepository.deleteById(id);
    }
}