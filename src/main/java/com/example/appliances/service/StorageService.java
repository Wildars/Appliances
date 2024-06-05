package com.example.appliances.service;

import com.example.appliances.entity.Product;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.StorageResponseBot;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageService {

    StorageResponse create(StorageRequest storageRequest);

     StorageResponse findById(Long id);

    public Page<StorageResponse> getAllStorage(int page,
                                               int size,
                                               Optional<Boolean> sortOrder,
                                               String sortBy,
                                               Optional<Long> storageId);

    public int getAvailableQuantity(UUID productId);

    public void updateStockByProductId(UUID productId, int quantity);
    public void returnStockByProductId(UUID productId, int quantity);
    StorageResponse update(StorageRequest storageRequest, Long id);

     List<StorageResponse> findAll();

    public List<StorageResponseBot> findAllBot();

     void deleteById(Long id);

    public List<Product> getProductsById(List<UUID> productIds);


    public void checkProductAvailability(UUID productId, int requestedQuantity) ;


    public void updateStock(UUID productId, Long storageId, int quantity) ;
    public Product getProductById(UUID productId);
}
