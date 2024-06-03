package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.WishListFilialMapper;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.WishListFilialRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListFilialResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.*;
import com.example.appliances.service.WishListFilialService;
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
public class WishListServiceFilialImpl implements WishListFilialService {
    WishListFilialRepository wishListRepository;
    WishListFilialMapper wishListMapper;
    FilialRepository filialRepository;
    StorageRepository storageRepository;
    ProductRepository productRepository;
    @Autowired
    public WishListServiceFilialImpl(WishListFilialRepository wishListRepository, WishListFilialMapper wishListMapper, FilialRepository filialRepository, StorageRepository storageRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
        this.filialRepository = filialRepository;
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
    }
    @Override
    public Page<WishListFilialResponse> getAllPage(int page,
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
        Page<WishListFilial> saleItemsPage = wishListRepository.findAll(paging);

        return saleItemsPage.map(wishListMapper::entityToResponse);
    }

    @Override
    @Transactional
    public WishListFilialResponse create(WishListFilialRequest request) {
        WishListFilial wishList = wishListMapper.requestToEntity(request);

        // Validate and set filial
        Filial storage = filialRepository.findById(request.getFilialId())
                .orElseThrow(() -> new RecordNotFoundException("Filial not found"));
        wishList.setFilial(storage);

        // Map WishListItems
        List<WishListItemFilial> wishListItems = request.getWishListItemFilials().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new RecordNotFoundException("Product not found"));
                    return WishListItemFilial.builder()
                            .product(product)
                            .wishListFilial(wishList)
                            .quantity(itemRequest.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
        wishList.setWishListItemFilials(wishListItems);

        WishListFilial savedWishList = wishListRepository.save(wishList);
        return wishListMapper.entityToResponse(savedWishList);
    }
    @Override
    @Transactional
    public WishListFilialResponse findById(Long id) {
        WishListFilial product = wishListRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует!"));
        return wishListMapper.entityToResponse(product);
    }
    @Override
    @Transactional
    public WishListFilialResponse update(WishListFilialRequest request, Long productId) {
        WishListFilial product = wishListRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует"));
        wishListMapper.update(product, request);
        WishListFilial updatedProduct = wishListRepository.save(product);
        return wishListMapper.entityToResponse(updatedProduct);
    }
    @Override
    @Transactional
    public List<WishListFilialResponse> findAll() {
        List<WishListFilial> products = wishListRepository.findAll();
        return products.stream().map(wishListMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        wishListRepository.deleteById(id);
    }
}