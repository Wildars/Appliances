package com.example.appliances.api;

import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.response.SaleResponse;
import com.example.appliances.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleApi {

    private final SaleService saleService;

    @Autowired
    public SaleApi(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest saleRequest) {
        SaleResponse createdSale = saleService.create(saleRequest);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long id) {
        SaleResponse sale = saleService.findById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> updateSale(@RequestBody SaleRequest saleRequest, @PathVariable Long id) {
        SaleResponse updatedSale = saleService.update(saleRequest, id);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<SaleResponse> sales = saleService.findAll();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}