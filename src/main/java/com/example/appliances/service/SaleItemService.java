package com.example.appliances.service;

import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.request.SaleItemNowRequest;
import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.model.response.SaleResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SaleItemService {

    public SaleItemResponse create(SaleItemRequest saleItemRequest);
    public SaleItemResponse createNow(SaleItemNowRequest saleItemRequest);
    public SaleItemResponse findById(Long id);

    public void sendSaleItem(Long saleItemId, SaleItemElementRequest request);

    public void doneSaleItem(Long saleItemId, SaleItemElementRequest request);

    public void rejectSaleItem(Long queueEntryId, SaleItemElementRequest request);
    public Page<SaleItemResponse> getAllSaleItems(int page,
                                                  int size,
                                                  Optional<Boolean> sortOrder,
                                                  String sortBy);
    public SaleItemResponse update(SaleItemRequest saleItemRequest, Long saleId);

    public List<SaleItemResponse> findAll();

    public void deleteById(Long id);
}
