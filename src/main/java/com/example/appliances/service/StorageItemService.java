package com.example.appliances.service;

import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.model.response.StorageResponse;

import java.util.List;
import java.util.UUID;

public interface StorageItemService {
    public StorageItemResponse createStorageItem(StorageItemRequest storageItemRequest);

    public StorageItemResponse updateStorageItem(StorageItemRequest storageItemRequest, Long id);

    public List<StorageItemResponse> findAllStorageItems();

    public void deleteStorageItemById(Long id);

    StorageItemResponse findById(Long id);

    void updateStock(UUID productId, Long storageId, int quantity);

//    public void updateStockByProductId(UUID productId, int quantity);
}
