package com.example.appliances.api;

import com.example.appliances.model.request.FilialItemRequest;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.response.FilialItemResponse;
import com.example.appliances.model.response.StorageItemResponse;
import com.example.appliances.service.FilialItemService;
import com.example.appliances.service.StorageItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filialItem")
public class FilialItemApi {

    private final FilialItemService filialItemService;

    @Autowired
    public FilialItemApi(FilialItemService filialItemService) {
        this.filialItemService = filialItemService;
    }


    @GetMapping("/{filialId}/items")
    public ResponseEntity<List<FilialItemResponse>> getFilialItems(@PathVariable Long filialId) {
        List<FilialItemResponse> filialItems = filialItemService.getFilialItemsByFilialId(filialId);
        return ResponseEntity.ok(filialItems);
    }


    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<FilialItemResponse> create(@RequestBody FilialItemRequest storageRequest) {
        FilialItemResponse createdSale = filialItemService.create(storageRequest);
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
    public ResponseEntity<FilialItemResponse> getById(@PathVariable Long id) {
        FilialItemResponse sale = filialItemService.findById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<FilialItemResponse> update(@RequestBody FilialItemRequest storageRequest, @PathVariable Long id) {
        FilialItemResponse updatedSale = filialItemService.update(storageRequest, id);
        return new ResponseEntity<>(updatedSale, HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<FilialItemResponse>> getAll() {
        List<FilialItemResponse> sales = filialItemService.findAll();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        filialItemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}