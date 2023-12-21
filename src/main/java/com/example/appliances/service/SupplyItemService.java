package com.example.appliances.service;

import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.response.SupplyItemResponse;

import java.util.List;

public interface SupplyItemService {
    SupplyItemResponse create(SupplyItemRequest supplyItemRequest);

    SupplyItemResponse findById(Long id);

    SupplyItemResponse update(SupplyItemRequest supplyItemRequest, Long id);

    List<SupplyItemResponse> findAll();

    void deleteById(Long id);
}
