package com.example.appliances.service.impl;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.Storage;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.exception.ProductNotAvailableException;
import com.example.appliances.exception.ProductNotFoundException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.StorageItemMapper;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.repository.FilialItemRepository;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.repository.StorageItemRepository;
import com.example.appliances.repository.StorageRepository;
import com.example.appliances.service.StorageItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageItemServiceImpl implements StorageItemService {

    private final ProductRepository productRepository;

    private final FilialItemRepository filialItemRepository;
    @Autowired
    private final StorageItemRepository storageItemRepository;
    @Autowired
    private final StorageRepository storageRepository;
    @Autowired
    private final StorageItemMapper storageItemMapper;

    public StorageItemServiceImpl(ProductRepository productRepository, FilialItemRepository filialItemRepository, StorageItemRepository storageItemRepository, StorageRepository storageRepository, StorageItemMapper storageItemMapper) {
        this.productRepository = productRepository;
        this.filialItemRepository = filialItemRepository;
        this.storageItemRepository = storageItemRepository;
        this.storageRepository = storageRepository;
        this.storageItemMapper = storageItemMapper;
    }

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
        storageItem.setQuantity(storageItemRequest.getQuantity()); // Обновление количества
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
        log.info("Attempting to update stock for productId: {}, storageId: {}, quantity: {}", productId, storageId, quantity);
        try {
            StorageItem storageItem = storageItemRepository.findByProductIdAndStorageId(productId, storageId)
                    .orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));

            log.info("Current stock for productId: {}, storageId: {} is {}", productId, storageId, storageItem.getQuantity());
            int newQuantity = storageItem.getQuantity() + quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Недостаточно товара на складе");
            }

            storageItem.setQuantity(newQuantity);
            StorageItem updatedItem = storageItemRepository.save(storageItem);
            log.info("Updated stock for productId: {}, storageId: {} to {}", productId, storageId, updatedItem.getQuantity());
        } catch (RecordNotFoundException e) {
            // Создаем новый StorageItem если он не существует
            StorageItem newItem = new StorageItem();
            newItem.setProduct(productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар с указанным ID не найден")));
            newItem.setStorage(storageRepository.findById(storageId)
                    .orElseThrow(() -> new IllegalArgumentException("Склад с указанным ID не найден")));
            newItem.setQuantity(quantity);
            storageItemRepository.save(newItem);
            log.info("Created new storage item for productId: {}, storageId: {}, quantity: {}", productId, storageId, quantity);
        }
    }


    @Override
    @Transactional
    public void checkProductAvailability(UUID productId,Long storageId, int requestedQuantity) {
        // Найдем запись StorageItem для заданного productId
        StorageItem storageItem = storageItemRepository.findByProductIdAndStorageId(productId,storageId).orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));;

        // Проверим, найден ли StorageItem
        if (storageItem == null) {
            throw new ProductNotFoundException("Продукт с ID " + productId + " не найден на складе");
        }

        // Проверим доступное количество
        if (storageItem.getQuantity() < requestedQuantity) {
            throw new ProductNotAvailableException("Недостаточно товара на складе. Доступное количество: " + storageItem.getQuantity());
        }
    }

    @Override
    @Transactional
    public void updateStockByProductId(UUID productId,Long storageId ,int quantity) {
        // Получаем информацию о товаре на складе по productId
        StorageItem storageItem = storageItemRepository.findByProductIdAndStorageId(productId,storageId).orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));;;

        if (storageItem == null) {
            throw new ProductNotFoundException("Товар не найден на складе с ID: " + productId);
        }

        int newQuantity = storageItem.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        storageItem.setQuantity(newQuantity);
        storageItemRepository.save(storageItem);
    }


    @Override
    @Transactional
    public void updateStockInFilial(UUID productId, Long filialId, int quantity) {
        // Получаем информацию о товаре на филиале по productId и filialId
        FilialItem filialItem = filialItemRepository.findByProductIdAndFilialId(productId, filialId)
                .orElseThrow(() -> new RecordNotFoundException("Товар не найден на филиале с ID: " + productId));

        // Проверяем, что количество на филиале достаточно для выполнения операции
        int newFilialQuantity = filialItem.getQuantity() - quantity;
        if (newFilialQuantity < 0) {
            throw new IllegalArgumentException("Недостаточно товара на филиале");
        }
        filialItem.setQuantity(newFilialQuantity);
        filialItemRepository.save(filialItem);
    }

    // Реализация метода findByProductIdAndFilialId
    @Override
    @Transactional
    public StorageItem findByProductIdAndStorageId(UUID productId, Long storageId) {
        return storageItemRepository.findByProductIdAndStorageId(productId, storageId)
                .orElseThrow(() -> new RecordNotFoundException("Товар не найден на складе"));
    }
}