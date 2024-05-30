package com.example.appliances.service;

import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SupplyItemService {
    SupplyItemResponse create(SupplyItemRequest supplyItemRequest);

    SupplyItemResponse findById(Long id);

    SupplyItemResponse update(SupplyItemRequest supplyItemRequest, Long id);

    List<SupplyItemResponse> findAll();

    void deleteById(Long id);

    public Page<SupplyItemResponse> getAllSupplierItemPages(int page,
                                                int size,
                                                Optional<Boolean> sortOrder,
                                                String sortBy);
}
