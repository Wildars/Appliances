package com.example.appliances.service.impl;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.exception.ProductNotAvailableException;
import com.example.appliances.exception.ProductNotFoundException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.FilialItemMapper;
import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.repository.FilialItemRepository;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.service.FilialItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilialItemServiceImpl implements FilialItemService {

    @Autowired
    private FilialItemRepository filialItemRepository;

    @Autowired
    private FilialItemMapper filialItemMapper;

    @Autowired
    ProductRepository productRepository;

    @Override
    @Transactional
    public FilialItem getFilialItemById(Long id) {
        return filialItemRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Товар не найден на складе с ID: " + id));
    }
    @Override
    @Transactional
    public FilialItem save(FilialItem filialItem) {
        return filialItemRepository.save(filialItem);
    }
    @Override
    @Transactional
    public FilialItemResponse create(FilialItemRequest request) {
        FilialItem storageItem = filialItemMapper.requestToEntity(request);
        FilialItem savedStorageItem = filialItemRepository.save(storageItem);
        return filialItemMapper.entityToResponse(savedStorageItem);
    }

    @Override
    @Transactional
    public FilialItem updateEntity(FilialItem filialItem) {
        return filialItemRepository.save(filialItem);  // Обновление сущности
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
    public List<FilialItemResponse> getFilialItemsByFilialId(Long filialId) {
        List<FilialItem> filialItems = filialItemRepository.findByFilialId(filialId);
        return filialItems.stream().map(filialItemMapper::entityToResponse).collect(Collectors.toList());
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
    @Override
    @Transactional
    public void updateStockByProductId(Long id, int quantity) {
        // Получаем информацию о товаре в филиале по id
        FilialItem filialItem = filialItemRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Товар не найден на складе с ID: " + id));

        int newQuantity = filialItem.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        filialItem.setQuantity(newQuantity);
        filialItemRepository.save(filialItem);
    }

    @Override
    @Transactional
    public List<Product> getProductsById(List<UUID> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }

    @Override
    @Transactional
    public void checkProductAvailability(Long filialItemId, int requestedQuantity) {
        // Найдем запись FilialItem для заданного id
        Optional<FilialItem> filialItem = filialItemRepository.findById(filialItemId);

        // Проверим, найден ли FilialItem
        if (filialItem.isEmpty()) {
            throw new ProductNotFoundException("Продукт с ID " + filialItemId + " не найден");
        }

        // Проверим доступное количество
        if (filialItem.get().getQuantity() < requestedQuantity) {
            throw new ProductNotAvailableException("Недостаточно товара на складе. Доступное количество: " + filialItem.get().getQuantity());
        }
    }


    @Override
    @Transactional
    public FilialItem findByProductIdAndFilialId(UUID productId, Long filialId) {
        return filialItemRepository.findByProductIdAndFilialId(productId, filialId)
                .orElseThrow(() -> new RecordNotFoundException("Товар не найден в филиале"));
    }

    @Override
    @Transactional
    public void create(FilialItem filialItem) {
        filialItemRepository.save(filialItem);
    }


}