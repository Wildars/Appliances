package com.example.appliances.service.impl;


import com.example.appliances.entity.Product;
import com.example.appliances.entity.SaleItem;
import com.example.appliances.entity.Storage;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.exception.ProductNotAvailableException;
import com.example.appliances.exception.ProductNotFoundException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.StorageMapper;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.StorageResponseBot;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.repository.StorageItemRepository;
import com.example.appliances.repository.StorageRepository;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.StorageItemService;
import com.example.appliances.service.StorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StorageServiceImpl implements StorageService {
    StorageMapper storageMapper;

    StorageRepository storageRepository;

    StorageItemService storageItemService;

    ProductService productService;

    ProductRepository productRepository;

    StorageItemRepository storageItemRepository;

    public StorageServiceImpl(StorageMapper storageMapper, StorageRepository storageRepository, StorageItemService storageItemService, ProductService productService, ProductRepository productRepository, StorageItemRepository storageItemRepository) {
        this.storageMapper = storageMapper;
        this.storageRepository = storageRepository;
        this.storageItemService = storageItemService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.storageItemRepository = storageItemRepository;
    }

    @Override
    @Transactional
    public Page<StorageResponse> getAllStorage(int page,
                                               int size,
                                               Optional<Boolean> sortOrder,
                                               String sortBy,
                                               Optional<Long> storageId) {
        Pageable paging;

        // Если storageId задан, фильтруем по нему
        if (storageId.isPresent()) {
            Page<Storage> storagePage = storageRepository.findById(storageId.get(), PageRequest.of(page, size));
            return storagePage.map(storageMapper::entityToResponse);
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

        Page<Storage> storagePage = storageRepository.findAll(paging);
        return storagePage.map(storageMapper::entityToResponse);
    }
    @Override
    @Transactional
    public StorageResponse create(StorageRequest storageRequest) {
        Storage storage = storageMapper.requestToEntity(storageRequest);
        String newFilCode = generateNextFilCode();
        storage.setStorageCode(newFilCode);
        Storage savedStorage = storageRepository.save(storage);
        return storageMapper.entityToResponse(savedStorage);
    }

    private String generateNextFilCode() {
        String minFilialCode = "00001";


        List<String> existingScreens = storageRepository.findAllStorageCodes();

        if (!existingScreens.isEmpty()) {
            //мин значение с бд
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                String nextScreenValue = String.format("%05d", i);
                if (!existingScreens.contains(nextScreenValue)) {
                    minFilialCode = nextScreenValue;
                    break;
                }
            }
        }

        return minFilialCode;
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
    public List<StorageResponseBot> findAllBot() {
        List<Storage> sales = storageRepository.findAll();
        return sales.stream().map(storageMapper::entityToResponseBot).collect(Collectors.toList());
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
    public List<Product> getProductsById(List<UUID> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }

    @Override
    @Transactional
    public void checkProductAvailability(UUID productId, int requestedQuantity) {
        // Найдем запись StorageItem для заданного productId
        StorageItem storageItem = storageItemRepository.findByProductId(productId);

        // Проверим, найден ли StorageItem
        if (storageItem == null) {
            throw new ProductNotFoundException("Продукт с ID " + productId + " не найден");
        }

        // Проверим доступное количество
        if (storageItem.getQuantity() < requestedQuantity) {
            throw new ProductNotAvailableException("Недостаточно товара на складе. Доступное количество: " + storageItem.getQuantity());
        }
    }
    @Override
    @Transactional(readOnly = true)
    public int getAvailableQuantity(UUID productId) {
        // Получаем информацию о товаре на складе
        StorageItem storageItem = storageItemRepository.findByProductId(productId);

        // Если информация не найдена, считаем количество равным нулю
        return storageItem != null ? storageItem.getQuantity() : 0;
    }
    @Transactional
    @Override
    public void returnStockByProductId(UUID productId, int quantity) {
        // Получаем информацию о товаре на складе по productId
        StorageItem storageItem = storageItemRepository.findByProductId(productId);

        if (storageItem == null) {
            throw new ProductNotFoundException("Товар не найден на складе с ID: " + productId);
        }

        int newQuantity = storageItem.getQuantity() + quantity;
        storageItem.setQuantity(newQuantity);
        storageItemRepository.save(storageItem);
    }

    @Override
    @Transactional
    public void updateStockByProductId(UUID productId, int quantity) {
        // Получаем информацию о товаре на складе по productId
        StorageItem storageItem = storageItemRepository.findByProductId(productId);

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
    public void updateStock(UUID productId, Long storageId, int quantity) {
        storageItemService.updateStock(productId, storageId, quantity);
        productService.updateStock(productId, quantity);
    }

    @Override
    @Transactional
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + productId + " не найден"));
    }
}
