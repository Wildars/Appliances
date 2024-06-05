package com.example.appliances.service.impl;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.Storage;
import com.example.appliances.entity.WishList;
import com.example.appliances.entity.WishListItem;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.repository.StorageRepository;
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

    StorageRepository storageRepository;
    ProductRepository productRepository;
    @Autowired
    public WishListServiceImpl(WishListRepository wishListRepository, WishListMapper wishListMapper, StorageRepository storageRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
    }
    @Override
    public Page<WishListResponse> getAllByPage(int page,
                                                        int size,
                                                        Optional<Boolean> sortOrder,
                                                        String sortBy,
                                                        Optional<Long> storageId) {
        Pageable paging;

        // Если storageId задан, фильтруем по нему
        if (storageId.isPresent()) {
            Page<WishList> wishListPage = wishListRepository.findByStorageId(storageId.get(), PageRequest.of(page, size));
            return wishListPage.map(wishListMapper::entityToResponse);
        }

        // Если sortBy равен "id", мы хотим сортировать по ID
        if ("id".equals(sortBy)) {
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, "id");
        } else {
            if (sortOrder.isPresent()) {
                Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
                paging = PageRequest.of(page, size, direction, sortBy);
            } else {
                paging = PageRequest.of(page, size);
            }
        }

        Page<WishList> wishListPage = wishListRepository.findAll(paging);
        return wishListPage.map(wishListMapper::entityToResponse);
    }

    @Override
    @Transactional
    public WishListResponse create(WishListRequest wishListRequest) {
        WishList wishList = wishListMapper.requestToEntity(wishListRequest);

        wishList.setIsServed(false);
        // Validate and set storage
        Storage storage = storageRepository.findById(wishListRequest.getStorageId())
                .orElseThrow(() -> new RecordNotFoundException("Storage not found"));
        wishList.setStorage(storage);

        // Map WishListItems
        List<WishListItem> wishListItems = wishListRequest.getWishListItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new RecordNotFoundException("Product not found"));
                    return WishListItem.builder()
                            .product(product)
                            .wishList(wishList)
                            .quantity(itemRequest.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
        wishList.setWishListItems(wishListItems);

        WishList savedWishList = wishListRepository.save(wishList);
        return wishListMapper.entityToResponse(savedWishList);
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