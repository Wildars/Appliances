package com.example.appliances.service;

import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;

import java.util.List;

public interface ProductCategoryService {

    public ProductCategoryResponse create(ProductCategoryRequest productCategoryRequest);

    public ProductCategoryResponse findById(Long id) ;

    public ProductCategoryResponse update(ProductCategoryRequest productCategoryRequest, Long productId);

    public List<ProductCategoryResponse> findAll();

    public void deleteById(Long id) ;
}
