package com.example.appliances.service.impl;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.FilialItemMapper;
import com.example.appliances.mapper.StorageItemMapper;
import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.repository.FilialItemRepository;
import com.example.appliances.repository.StorageItemRepository;
import com.example.appliances.service.FilialItemService;
import com.example.appliances.service.StorageItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilialItemServiceImpl implements FilialItemService {

    @Autowired
    private FilialItemRepository filialItemRepository;

    @Autowired
    private FilialItemMapper filialItemMapper;



    @Override
    @Transactional
    public FilialItemResponse create(FilialItemRequest request) {
        FilialItem storageItem = filialItemMapper.requestToEntity(request);
        FilialItem savedStorageItem = filialItemRepository.save(storageItem);
        return filialItemMapper.entityToResponse(savedStorageItem);
    }

    @Override
    @Transactional
    public FilialItemResponse update(FilialItemRequest request, Long id) {
        FilialItem storageItem = filialItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Элемент склада с таким id не существует"));
        filialItemMapper.update(storageItem, request);
        FilialItem updatedStorageItem = filialItemRepository.save(storageItem);
        return filialItemMapper.entityToResponse(updatedStorageItem);
    }

    @Override
    @Transactional
    public List<FilialItemResponse> findAll() {
        List<FilialItem> storageItems = filialItemRepository.findAll();
        return storageItems.stream().map(filialItemMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        filialItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public FilialItemResponse findById(Long id) {
        FilialItem sale = filialItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует!"));
        return filialItemMapper.entityToResponse(sale);
    }

    @Override
    @Transactional
    public void updateStock(UUID productId, Long storageId, int quantity) {
        FilialItem storageItem = filialItemRepository.findByProductIdAndFilialId(productId, storageId)
                .orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));

        int newQuantity = storageItem.getQuantity() + quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        storageItem.setQuantity(newQuantity);
        filialItemRepository.save(storageItem);
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