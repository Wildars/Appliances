package com.example.appliances.service;

import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageItemResponse;

import java.util.List;

public interface StorageItemService {
    public StorageItemResponse createStorageItem(StorageItemRequest storageItemRequest);

    public StorageItemResponse updateStorageItem(StorageItemRequest storageItemRequest, Long id);

    public List<StorageItemResponse> findAllStorageItems();

    public void deleteStorageItemById(Long id);

    void updateStock(Long productId, Long storageId, int quantity);

    public void updateStockByProductId(Long productId, int quantity);
}
