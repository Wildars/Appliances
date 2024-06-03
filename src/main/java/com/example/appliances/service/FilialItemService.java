package com.example.appliances.service;

import com.example.appliances.entity.FilialItem;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.StorageItem;
import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.StorageItemResponse;

import java.util.List;
import java.util.UUID;

public interface FilialItemService {

    public FilialItem getFilialItemById(Long id);
    public FilialItemResponse create(FilialItemRequest request);

    public FilialItemResponse update(FilialItemRequest request, Long id);

    public FilialItem save(FilialItem filialItem);

    public List<FilialItemResponse> findAll();

    public FilialItem updateEntity(FilialItem filialItem);

    public void deleteById(Long id);

    FilialItemResponse findById(Long id);

    void updateStock(UUID productId, Long filialId, int quantity);

    public List<Product> getProductsById(List<UUID> productIds);

    public void updateStockByProductId(Long id, int quantity);

    public void checkProductAvailability(Long filialItemId, int requestedQuantity);

    public FilialItem findByProductIdAndFilialId(UUID productId, Long storageId);

    public List<FilialItemResponse> getFilialItemsByFilialId(Long filialId);

    void create(FilialItem newFilialItem);
}
