package com.example.appliances.api;

import com.example.appliances.model.request.WishListFilialRequest;
import com.example.appliances.model.request.WishListRequest;
import com.example.appliances.model.response.WishListFilialResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.WishListFilialService;
import com.example.appliances.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist/filials")
public class WishListFilialApi {

    private final WishListFilialService wishListFilialService;

    @Autowired
    public WishListFilialApi(WishListFilialService wishListFilialService) {
        this.wishListFilialService = wishListFilialService;
    }


    @GetMapping("/page")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<WishListFilialResponse> findAllBySpecification(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int size,
            @RequestParam(required = false) Optional<Boolean> sortOrder,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Optional<Long> filialId) {
        return wishListFilialService.getAllPage(page, size, sortOrder, sortBy, filialId);
    }
    @PostMapping
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListFilialResponse> create(@RequestBody WishListFilialRequest wishListRequest) {
        WishListFilialResponse createdProduct = wishListFilialService.create(wishListRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListFilialResponse> getById(@PathVariable Long id) {
        WishListFilialResponse product = wishListFilialService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PatchMapping("/return/{orderId}")
    public ResponseEntity<String> returnWishList(@PathVariable Long orderId) {
        try {
            wishListFilialService.returnWishList(orderId);
            return ResponseEntity.ok("WishList returned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during returning WishList: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<WishListFilialResponse> update(@RequestBody WishListFilialRequest wishListRequest, @PathVariable Long id) {
        WishListFilialResponse updatedProduct = wishListFilialService.update(wishListRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/list")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<WishListFilialResponse>> getAll() {
        List<WishListFilialResponse> products = wishListFilialService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishListFilialService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}