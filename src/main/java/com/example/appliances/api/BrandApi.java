package com.example.appliances.api;

import com.example.appliances.model.request.BrandRequest;
import com.example.appliances.model.response.BrandResponse;
import com.example.appliances.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandApi {
    private final BrandService brandService;
    @Autowired
    public BrandApi(BrandService brandService) {
        this.brandService = brandService;
    }


    @PostMapping("/create")
    public ResponseEntity<BrandResponse> create(@RequestBody BrandRequest request) {
        BrandResponse createdProductField = brandService.create(request);
        return new ResponseEntity<>(createdProductField, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(@PathVariable Long id) {
        BrandResponse productField = brandService.findById(id);
        return new ResponseEntity<>(productField, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<BrandResponse>> getAll() {
        List<BrandResponse> products = brandService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(@RequestBody BrandRequest request, @PathVariable Long id) {
        BrandResponse updatedProductField = brandService.update(request, id);
        return new ResponseEntity<>(updatedProductField, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
