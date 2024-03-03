package com.example.appliances.api;

import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.request.SaleItemNowRequest;
import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.model.response.UserResponse;
import com.example.appliances.service.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale-items")
public class SaleItemApi {

    private final SaleItemService saleItemService;

    @Autowired
    public SaleItemApi(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<SaleItemResponse> createSaleItem(@RequestBody SaleItemRequest saleItemRequest) {
        SaleItemResponse createdSaleItem = saleItemService.create(saleItemRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }


    @PostMapping("/now")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<SaleItemResponse> createSaleItemNow(@RequestBody SaleItemNowRequest saleItemRequest) {
        SaleItemResponse createdSaleItem = saleItemService.createNow(saleItemRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<SaleItemResponse>> getAllSaleItemsByStatus(@RequestParam Long saleStatusId) {
        List<SaleItemResponse> saleItems = saleItemService.getAllSaleItemsByStatus(saleStatusId);
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<SaleItemResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "25") int size,
                                                     @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                     @RequestParam(required = false) String sortBy) {
        return saleItemService.getAllSaleItems(page, size, sortOrder, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<SaleItemResponse> getSaleItemById(@PathVariable Long id) {
        SaleItemResponse saleItem = saleItemService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<SaleItemResponse> updateSaleItem(@RequestBody SaleItemRequest saleItemRequest, @PathVariable Long id) {
        SaleItemResponse updatedSaleItem = saleItemService.update(saleItemRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<SaleItemResponse>> getAllSaleItems() {
        List<SaleItemResponse> saleItems = saleItemService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long id) {
        saleItemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PutMapping("/{saleItemId}/sendet")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> sendetSaleItem(
            @PathVariable Long saleItemId,
            @RequestBody SaleItemElementRequest request) {
        saleItemService.sendSaleItem(saleItemId, request);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{saleItemId}/done")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> doneSaleItem(
            @PathVariable Long saleItemId,
            @RequestBody SaleItemElementRequest request) {
        saleItemService.doneSaleItem(saleItemId, request);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{saleItemId}/reject")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> rejectSaleItem(
            @PathVariable Long saleItemId,
            @RequestBody SaleItemElementRequest request) {
        saleItemService.rejectSaleItem(saleItemId, request);
        return ResponseEntity.ok().build();
    }

}