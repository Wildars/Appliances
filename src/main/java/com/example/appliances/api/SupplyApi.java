package com.example.appliances.api;

import com.example.appliances.entity.Supply;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers")
public class SupplyApi {

    private final SupplyService supplyService;

    @Autowired
    public SupplyApi(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<SupplyResponse> createProvider(@RequestBody SupplyRequest supplyRequest) {
        SupplyResponse createdSaleItem = supplyService.create(supplyRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<SupplyResponse> getProviderById(@PathVariable Long id) {
        SupplyResponse saleItem = supplyService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<SupplyResponse> updateProvider(@RequestBody SupplyRequest supplyRequest, @PathVariable Long id) {
        SupplyResponse updatedSaleItem = supplyService.update(supplyRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    @GetMapping("/list")
    public Page<SupplyResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "25") int size,
                                                        @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                        @RequestParam(required = false) String sortBy) {
        return supplyService.getAllSuppliers(page, size, sortOrder, sortBy);
    }
    @GetMapping("/alls")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public List<Supply> getAllSupplies() {
        return supplyService.findAlls();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<List<SupplyItemResponse>> getAllProviders() {
        List<SupplyItemResponse> saleItems = supplyService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        supplyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @GetMapping("/wishlist")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<List<WishListResponse>> getAllWishListItems() {
        List<WishListResponse> wishListItems = supplyService.getAllWishListItems();
        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
    }

    @GetMapping("/wishlist/paged")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<Page<WishListResponse>> getAllWishListItemsPaged(@RequestParam(required = false, defaultValue = "0") int page,
                                                                           @RequestParam(required = false, defaultValue = "25") int size,
                                                                           @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                                           @RequestParam(required = false) String sortBy) {
        Page<WishListResponse> wishListItems = supplyService.getAllWishListItemsPaged(page, size, sortOrder, sortBy);
        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
    }
}