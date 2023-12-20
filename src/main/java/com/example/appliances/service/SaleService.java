package com.example.appliances.service;

import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.response.SaleResponse;

import java.util.List;

public interface SaleService {

    public SaleResponse create(SaleRequest saleRequest);

    public SaleResponse findById(Long id);

    public SaleResponse update(SaleRequest saleRequest, Long saleId);

    public List<SaleResponse> findAll();

    public void deleteById(Long id);
}
