package com.example.appliances.api;

import com.example.appliances.model.request.ProductFieldRequest;
import com.example.appliances.model.response.ProductFieldResponse;
import com.example.appliances.service.ProductFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-field")
public class ProductFieldApi {

    private final ProductFieldService productFieldService;

    @Autowired
    public ProductFieldApi(ProductFieldService productFieldService) {
        this.productFieldService = productFieldService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductFieldResponse> createProductField(@RequestBody ProductFieldRequest productFieldRequest) {
        ProductFieldResponse createdProductField = productFieldService.create(productFieldRequest);
        return new ResponseEntity<>(createdProductField, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFieldResponse> getProductFieldById(@PathVariable Long id) {
        ProductFieldResponse productField = productFieldService.findById(id);
        return new ResponseEntity<>(productField, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductFieldResponse>> getAllProductFields() {
        List<ProductFieldResponse> products = productFieldService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductFieldResponse> updateProductField(@RequestBody ProductFieldRequest productFieldRequest, @PathVariable Long id) {
        ProductFieldResponse updatedProductField = productFieldService.update(productFieldRequest, id);
        return new ResponseEntity<>(updatedProductField, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductField(@PathVariable Long id) {
        productFieldService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
