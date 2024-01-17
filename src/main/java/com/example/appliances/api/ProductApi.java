package com.example.appliances.api;

import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
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
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    @Autowired
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public Page<ProductResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "25") int size,
                                                                @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                                @RequestParam(required = false) String sortBy) {
        return productService.getAllProduct(page, size, sortOrder, sortBy);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.create(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        ProductResponse updatedProduct = productService.update(productRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}