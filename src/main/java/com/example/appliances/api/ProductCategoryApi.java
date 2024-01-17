package com.example.appliances.api;

import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.service.ProductCategoryService;
import com.example.appliances.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products/category")
public class ProductCategoryApi {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryApi(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }


    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<ProductCategoryResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "25") int size,
                                                                @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                                @RequestParam(required = false) String sortBy) {
        return productCategoryService.getAllProductCategory(page, size, sortOrder, sortBy);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductCategoryResponse> createProductCategory(@RequestBody ProductCategoryRequest productRequest) {
        ProductCategoryResponse createdProduct = productCategoryService.create(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductCategoryResponse> getProductCategoryById(@PathVariable Long id) {
        ProductCategoryResponse product = productCategoryService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductCategoryResponse> updateProductCategory(@RequestBody ProductCategoryRequest productRequest, @PathVariable Long id) {
        ProductCategoryResponse updatedProduct = productCategoryService.update(productRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<ProductCategoryResponse>> getAllProductsCategory() {
        List<ProductCategoryResponse> products = productCategoryService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        productCategoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}