package com.example.appliances.api;

import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.model.response.SaleResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/storage")
public class StorageApi {

    private final StorageService storageService;

    @Autowired
    public StorageApi(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageResponse> createStorage(@RequestBody StorageRequest storageRequest) {
        StorageResponse createdSale = storageService.create(storageRequest);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }


    @GetMapping("/page")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<StorageResponse> findAllBySpecification(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int size,
            @RequestParam(required = false) Optional<Boolean> sortOrder,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Optional<Long> storageId) {
        return storageService.getAllStorage(page, size, sortOrder, sortBy, storageId);
    }
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageResponse> getStorageById(@PathVariable Long id) {
        StorageResponse sale = storageService.findById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageResponse> updateStorage(@RequestBody StorageRequest storageRequest, @PathVariable Long id) {
        StorageResponse updatedSale = storageService.update(storageRequest, id);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<StorageResponse>> getAllStorage() {
        List<StorageResponse> sales = storageService.findAll();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}