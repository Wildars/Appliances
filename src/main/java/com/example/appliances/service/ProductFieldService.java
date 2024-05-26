package com.example.appliances.service;
import com.example.appliances.model.request.ProductFieldRequest;
import com.example.appliances.model.response.ProductFieldResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductFieldService {
    Page<ProductFieldResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy);
    ProductFieldResponse create(ProductFieldRequest productFieldRequest);
    ProductFieldResponse findById(Long id);
    ProductFieldResponse update(ProductFieldRequest productFieldRequest, Long id);
    List<ProductFieldResponse> findAll();
    void deleteById(Long id);
}
