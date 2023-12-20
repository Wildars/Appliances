package com.example.appliances.api;

import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.service.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-items")
public class SaleItemApi {

    private final SaleItemService saleItemService;

    @Autowired
    public SaleItemApi(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @PostMapping
    public ResponseEntity<SaleItemResponse> createSaleItem(@RequestBody SaleItemRequest saleItemRequest) {
        SaleItemResponse createdSaleItem = saleItemService.create(saleItemRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleItemResponse> getSaleItemById(@PathVariable Long id) {
        SaleItemResponse saleItem = saleItemService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleItemResponse> updateSaleItem(@RequestBody SaleItemRequest saleItemRequest, @PathVariable Long id) {
        SaleItemResponse updatedSaleItem = saleItemService.update(saleItemRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SaleItemResponse>> getAllSaleItems() {
        List<SaleItemResponse> saleItems = saleItemService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long id) {
        saleItemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}