package com.example.appliances.service;

import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductResponse;

import java.util.List;

public interface ProductService {

    public ProductResponse create(ProductRequest productRequest);

    public ProductResponse findById(Long id) ;

    public ProductResponse update(ProductRequest productRequest, Long productId);

    public List<ProductResponse> findAll();

    public void deleteById(Long id) ;
}
