package com.example.appliances.service;

import com.example.appliances.entity.Product;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageResponse;

import java.util.List;

public interface StorageService {

    StorageResponse create(StorageRequest storageRequest);

     StorageResponse findById(Long id);

     StorageResponse update(StorageRequest storageRequest, Long id);

     List<StorageResponse> findAll();

     void deleteById(Long id);



    public void updateStock(Long productId, Long storageId, int quantity) ;
    public ProductResponse getProductById(Long storageId, Long productId);
}
