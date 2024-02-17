package com.example.appliances.service;

import com.example.appliances.entity.Product;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StorageService {

    StorageResponse create(StorageRequest storageRequest);

     StorageResponse findById(Long id);

    public Page<StorageResponse> getAllStorage(int page,
                                                 int size,
                                                 Optional<Boolean> sortOrder,
                                                 String sortBy);

    public int getAvailableQuantity(Long productId);

    public void updateStockByProductId(Long productId, int quantity);
    public void returnStockByProductId(Long productId, int quantity);
    StorageResponse update(StorageRequest storageRequest, Long id);

     List<StorageResponse> findAll();

     void deleteById(Long id);

    public List<Product> getProductsById(List<Long> productIds);


    public void checkProductAvailability(Long productId, int requestedQuantity) ;


    public void updateStock(Long productId, Long storageId, int quantity) ;
    public Product getProductById(Long productId);
}
