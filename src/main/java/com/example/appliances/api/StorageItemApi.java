package com.example.appliances.api;

import com.example.appliances.entity.StorageItem;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.request.StorageRequest;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.service.StorageItemService;
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
@RequestMapping("/api/storageItem")
public class StorageItemApi {

    private final StorageItemService storageItemService;

    @Autowired
    public StorageItemApi(StorageItemService storageItemService) {
        this.storageItemService = storageItemService;
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageItemResponse> create(@RequestBody StorageItemRequest storageRequest) {
        StorageItemResponse createdSale = storageItemService.createStorageItem(storageRequest);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }


//    @GetMapping("/list")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
//    public Page<StorageResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
//                                                         @RequestParam(required = false, defaultValue = "25") int size,
//                                                         @RequestParam(required = false) Optional<Boolean> sortOrder,
//                                                         @RequestParam(required = false) String sortBy) {
//        return storageItemService.getAllStorage(page, size, sortOrder, sortBy);
//    }
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageItemResponse> getById(@PathVariable Long id) {
        StorageItemResponse sale = storageItemService.findById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<StorageItemResponse> update(@RequestBody StorageItemRequest storageRequest, @PathVariable Long id) {
        StorageItemResponse updatedSale = storageItemService.updateStorageItem(storageRequest, id);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<StorageItemResponse>> getAll() {
        List<StorageItemResponse> sales = storageItemService.findAllStorageItems();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        storageItemService.deleteStorageItemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}