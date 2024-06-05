package com.example.appliances.api;

import com.example.appliances.entity.Supplier;
import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.SupplierService;
import com.example.appliances.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/postavshik")
public class SupplierApi {

    private final SupplierService supplierService;

    @Autowired
    public SupplierApi(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplierResponse> create(@RequestBody SupplierRequest supplyRequest) {
        SupplierResponse createdSaleItem = supplierService.create(supplyRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplierResponse> getById(@PathVariable Long id) {
        SupplierResponse saleItem = supplierService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplierResponse> update(@RequestBody SupplierRequest supplyRequest, @PathVariable Long id) {
        SupplierResponse updatedSaleItem = supplierService.update(supplyRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    @GetMapping("/list")
    public Page<SupplierResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "25") int size,
                                                        @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                        @RequestParam(required = false) String sortBy) {
        return supplierService.getAllSuppliers(page, size, sortOrder, sortBy);
    }

    @GetMapping("/supply_items")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<SupplierResponse>> getAll() {
        List<SupplierResponse> saleItems = supplierService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplierService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



//
//    @GetMapping("/wishlist")
////    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
//    public ResponseEntity<List<WishListResponse>> getAllWishListItems() {
//        List<WishListResponse> wishListItems = supplierService.getAllWishListItems();
//        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
//    }
//
//    @GetMapping("/wishlist/paged")
////    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
//    public ResponseEntity<Page<WishListResponse>> getAllWishListItemsPaged(@RequestParam(required = false, defaultValue = "0") int page,
//                                                                           @RequestParam(required = false, defaultValue = "25") int size,
//                                                                           @RequestParam(required = false) Optional<Boolean> sortOrder,
//                                                                           @RequestParam(required = false) String sortBy) {
//        Page<WishListResponse> wishListItems = supplierService.getAllWishListItemsPaged(page, size, sortOrder, sortBy);
//        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
//    }
}