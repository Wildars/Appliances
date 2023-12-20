package com.example.appliances.service;

import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.model.response.SaleResponse;

import java.util.List;

public interface SaleItemService {

    public SaleItemResponse create(SaleItemRequest saleItemRequest);

    public SaleItemResponse findById(Long id);

    public SaleItemResponse update(SaleItemRequest saleItemRequest, Long saleId);

    public List<SaleItemResponse> findAll();

    public void deleteById(Long id);
}
