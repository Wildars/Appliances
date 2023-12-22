package com.example.appliances.service;

import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {

    public ProductCategoryResponse create(ProductCategoryRequest productCategoryRequest);

    public ProductCategoryResponse findById(Long id) ;

    public Page<ProductCategoryResponse> getAllProductCategory(int page,
                                                               int size,
                                                               Optional<Boolean> sortOrder,
                                                               String sortBy);

    public ProductCategoryResponse update(ProductCategoryRequest productCategoryRequest, Long productId);

    public List<ProductCategoryResponse> findAll();

    public void deleteById(Long id) ;
}
