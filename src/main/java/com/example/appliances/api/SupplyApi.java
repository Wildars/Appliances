package com.example.appliances.api;

import com.example.appliances.entity.Supply;
import com.example.appliances.enums.SupplyStatus;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.SupplierService;
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

    private final SupplierService supplierService;

    @Autowired
    public SupplyApi(SupplyService supplyService, SupplierService supplierService) {
        this.supplyService = supplyService;
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplyResponse> createSupply(@RequestBody SupplyRequest supplyRequest,
                                                       @RequestHeader("X-Supplier-Pin") String supplierPin,
                                                       @RequestHeader("X-Supplier-Password") String supplierPassword) {
        // Проверяем валидность логина и пароля поставщика
        if (!supplierService.validateLogin(supplierPin, supplierPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Создаем поставку
        SupplyResponse createdSupply = supplyService.create(supplyRequest, supplierPin, supplierPassword);
        return new ResponseEntity<>(createdSupply, HttpStatus.CREATED);
    }


    @PostMapping("/confirmDelivery/{id}")
    public ResponseEntity<Void> confirmDelivery(@PathVariable Long id) {
        supplyService.confirmDelivery(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectSupply(@PathVariable Long id) {
        supplyService.rejectSupply(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplyResponse> getProviderById(@PathVariable Long id) {
        SupplyResponse saleItem = supplyService.findById(id);
        return new ResponseEntity<>(saleItem, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<SupplyResponse> updateProvider(@RequestBody SupplyRequest supplyRequest, @PathVariable Long id) {
        SupplyResponse updatedSaleItem = supplyService.update(supplyRequest, id);
        return new ResponseEntity<>(updatedSaleItem, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    @GetMapping("/page")
    public Page<SupplyResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "25") int size,
                                                       @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) Optional<Long> storageId,
                                                       @RequestParam(required = false) Optional<SupplyStatus> status,
                                                       @RequestParam(required = false) Optional<Long> supplierId) {
        return supplyService.getAllSuppliers(page, size, sortOrder, sortBy, storageId, status,supplierId);
    }

    @GetMapping("/supply_items")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<SupplyItemResponse>> getAllProviders() {
        List<SupplyItemResponse> saleItems = supplyService.findAll();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @GetMapping("/alls")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<SupplyResponse>>getAllSupplies() {
        List<SupplyResponse> saleItems = supplyService.findAlls();
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @GetMapping("/alls/telegram")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<SupplyItemResponse>>getAllItemTelegram(@RequestParam(required = false )String pin) {
        List<SupplyItemResponse> saleItems = supplyService.findAllBySupplierPin(pin);
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        supplyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @GetMapping("/wishlist")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<List<WishListResponse>> getAllWishListItems() {
        List<WishListResponse> wishListItems = supplyService.getAllWishListItems();
        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
    }

    @GetMapping("/wishlist/paged")
//    @PreAuthorize("hasAnyRole('ROLE_SUPPLIER','ROLE_ADMIN')")
    public ResponseEntity<Page<WishListResponse>> getAllWishListItemsPaged(@RequestParam(required = false, defaultValue = "0") int page,
                                                                           @RequestParam(required = false, defaultValue = "25") int size,
                                                                           @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                                           @RequestParam(required = false) String sortBy) {
        Page<WishListResponse> wishListItems = supplyService.getAllWishListItemsPaged(page, size, sortOrder, sortBy);
        return new ResponseEntity<>(wishListItems, HttpStatus.OK);
    }
}