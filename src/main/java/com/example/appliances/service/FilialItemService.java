package com.example.appliances.service;

import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.StorageItemResponse;

import java.util.List;
import java.util.UUID;

public interface FilialItemService {
    public FilialItemResponse create(FilialItemRequest request);

    public FilialItemResponse update(FilialItemRequest request, Long id);

    public List<FilialItemResponse> findAll();

    public void deleteById(Long id);

    FilialItemResponse findById(Long id);

    void updateStock(UUID productId, Long filialId, int quantity);

//    public void updateStockByProductId(UUID productId, int quantity);
}
