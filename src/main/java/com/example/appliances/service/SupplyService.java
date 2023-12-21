package com.example.appliances.service;

import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplyResponse;

import java.util.List;

public interface SupplyService {
     SupplyResponse create(SupplyRequest supplyRequest);

     SupplyResponse findById(Long id);

     SupplyResponse update(SupplyRequest supplyRequest, Long id);

     List<SupplyResponse> findAll();

     void deleteById(Long id);
}
