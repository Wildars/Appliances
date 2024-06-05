package com.example.appliances.api;

import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.ProductCategoryService;
import com.example.appliances.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
public class WishListApi {

    private final WishListService wishListService;

    @Autowired
    public WishListApi(WishListService wishListService) {
        this.wishListService = wishListService;
    }


    @GetMapping("/page")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<WishListResponse> findAllBySpecification(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int size,
            @RequestParam(required = false) Optional<Boolean> sortOrder,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Optional<Long> storageId) {
        return wishListService.getAllByPage(page, size, sortOrder, sortBy, storageId);
    }
    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListResponse> create(@RequestBody WishListRequest wishListRequest) {
        WishListResponse createdProduct = wishListService.create(wishListRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListResponse> getById(@PathVariable Long id) {
        WishListResponse product = wishListService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListResponse> update(@RequestBody WishListRequest wishListRequest, @PathVariable Long id) {
        WishListResponse updatedProduct = wishListService.update(wishListRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/list")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<WishListResponse>> getAll() {
        List<WishListResponse> products = wishListService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishListService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}