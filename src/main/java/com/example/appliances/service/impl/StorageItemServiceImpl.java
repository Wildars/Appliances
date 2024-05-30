package com.example.appliances.service.impl;

import com.example.appliances.entity.Storage;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.exception.ProductNotFoundException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.StorageItemMapper;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.repository.StorageItemRepository;
import com.example.appliances.service.StorageItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageItemServiceImpl implements StorageItemService {

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private StorageItemMapper storageItemMapper;

    @Override
    @Transactional
    public StorageItemResponse createStorageItem(StorageItemRequest storageItemRequest) {
        StorageItem storageItem = storageItemMapper.requestToEntity(storageItemRequest);
        StorageItem savedStorageItem = storageItemRepository.save(storageItem);
        return storageItemMapper.entityToResponse(savedStorageItem);
    }

    @Override
    @Transactional
    public StorageItemResponse updateStorageItem(StorageItemRequest storageItemRequest, Long id) {
        StorageItem storageItem = storageItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Элемент склада с таким id не существует"));
        storageItemMapper.update(storageItem, storageItemRequest);
        StorageItem updatedStorageItem = storageItemRepository.save(storageItem);
        return storageItemMapper.entityToResponse(updatedStorageItem);
    }

    @Override
    @Transactional
    public List<StorageItemResponse> findAllStorageItems() {
        List<StorageItem> storageItems = storageItemRepository.findAll();
        return storageItems.stream().map(storageItemMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteStorageItemById(Long id) {
        storageItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StorageItemResponse findById(Long id) {
        StorageItem sale = storageItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует!"));
        return storageItemMapper.entityToResponse(sale);
    }

    @Override
    @Transactional
    public void updateStock(UUID productId, Long storageId, int quantity) {
        StorageItem storageItem = storageItemRepository.findByProductIdAndStorageId(productId, storageId)
                .orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));

        int newQuantity = storageItem.getQuantity() + quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        storageItem.setQuantity(newQuantity);
        storageItemRepository.save(storageItem);
    }
//
//    @Override
//    @Transactional
//    public void updateStockByProductId(Long productId, int quantity) {
//        // Получаем информацию о товаре на складе по productId (если нужно)
//        StorageItem storageItem = storageItemRepository.findByProductId(productId);
//
//        if (storageItem == null) {
//            throw new ProductNotFoundException("Товар не найден на складе с ID: " + productId);
//        }
//
//        int newQuantity = storageItem.getQuantity() + quantity;
//        if (newQuantity < 0) {
//            throw new IllegalArgumentException("Недостаточно товара на складе");
//        }
//
//        storageItem.setQuantity(newQuantity);
//        storageItemRepository.save(storageItem);
//    }
}