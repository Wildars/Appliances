package com.example.appliances.service.impl;


import com.example.appliances.entity.Product;
import com.example.appliances.entity.Storage;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.StorageMapper;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.repository.StorageRepository;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.StorageItemService;
import com.example.appliances.service.StorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StorageServiceImpl implements StorageService {
    StorageMapper storageMapper;

    StorageRepository storageRepository;

    StorageItemService storageItemService;

    ProductService productService;

    public StorageServiceImpl(StorageMapper storageMapper, StorageRepository storageRepository, StorageItemService storageItemService, ProductService productService) {
        this.storageMapper = storageMapper;
        this.storageRepository = storageRepository;
        this.storageItemService = storageItemService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public StorageResponse create(StorageRequest storageRequest) {
        Storage storage = storageMapper.requestToEntity(storageRequest);
        Storage savedStorage = storageRepository.save(storage);
        return storageMapper.entityToResponse(savedStorage);
    }
    @Override
    @Transactional
    public StorageResponse findById(Long id) {
        Storage sale = storageRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует!"));
        return storageMapper.entityToResponse(sale);
    }
    @Override
    @Transactional
    public StorageResponse update(StorageRequest storageRequest, Long id) {
        Storage sale = storageRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует"));
        storageMapper.update(sale, storageRequest);
        Storage updatedSale = storageRepository.save(sale);
        return storageMapper.entityToResponse(updatedSale);
    }
    @Override
    @Transactional
    public List<StorageResponse> findAll() {
        List<Storage> sales = storageRepository.findAll();
        return sales.stream().map(storageMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        storageRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void updateStock(Long productId, Long storageId, int quantity) {
        storageItemService.updateStock(productId, storageId, quantity);
        productService.updateStock(productId, quantity);
    }

    @Override
    @Transactional
    public ProductResponse getProductById(Long storageId, Long productId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new RecordNotFoundException("Склад с таким id не существует"));

        return productService.getProductById(productId);
    }
}
