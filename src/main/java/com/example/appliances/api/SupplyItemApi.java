package com.example.appliances.api;

import com.example.appliances.entity.SupplyItem;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.SupplyItemService;
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
@RequestMapping("/api/providers/item")
public class SupplyItemApi {

    private final SupplyItemService supplyService;

    @Autowired
    public SupplyItemApi(SupplyItemService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplyItemResponse> create(@RequestBody SupplyItemRequest supplyRequest) {
        SupplyItemResponse createdSaleItem = supplyService.create(supplyRequest);
        return new ResponseEntity<>(createdSaleItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplyItemResponse> getById(@PathVariable Long id) {
        SupplyItemResponse saleItem = supplyService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplyItemResponse> updateProvider(@RequestBody SupplyItemRequest supplyRequest, @PathVariable Long id) {
        SupplyItemResponse updatedSaleItem = supplyService.update(supplyRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    @GetMapping("/page")
    public Page<SupplyItemResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "25") int size,
                                                        @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                        @RequestParam(required = false) String sortBy) {
        return supplyService.getAllSupplierItemPages(page, size, sortOrder, sortBy);
    }

    @GetMapping("/list/supply_items")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<SupplyItemResponse>> getAll() {
        List<SupplyItemResponse> saleItems = supplyService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        supplyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




//    @GetMapping("/wishlist")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
//    public ResponseEntity<List<WishListResponse>> getAllWishListItems() {
//        List<WishListResponse> wishListItems = supplyService.getAllWishListItems();
//        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
//    }
//
//    @GetMapping("/wishlist/paged")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
//    public ResponseEntity<Page<WishListResponse>> getAllWishListItemsPaged(@RequestParam(required = false, defaultValue = "0") int page,
//                                                                           @RequestParam(required = false, defaultValue = "25") int size,
//                                                                           @RequestParam(required = false) Optional<Boolean> sortOrder,
//                                                                           @RequestParam(required = false) String sortBy) {
//        Page<WishListResponse> wishListItems = supplyService.getAllWishListItemsPaged(page, size, sortOrder, sortBy);
//        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
//    }
}