package com.example.appliances.api;

import com.example.appliances.model.request.ProducingCountryRequest;
import com.example.appliances.model.response.ProducingCountryResponse;
import com.example.appliances.service.ProducingCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producing/country")
public class ProducingCountryApi {

    private final ProducingCountryService producingCountryService;
    @Autowired
    public ProducingCountryApi(ProducingCountryService producingCountryService) {
        this.producingCountryService = producingCountryService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProducingCountryResponse> createProductField(@RequestBody ProducingCountryRequest request) {
        ProducingCountryResponse createdProductField = producingCountryService.create(request);
        return new ResponseEntity<>(createdProductField, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducingCountryResponse> getProductFieldById(@PathVariable Long id) {
        ProducingCountryResponse productField = producingCountryService.findById(id);
        return new ResponseEntity<>(productField, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProducingCountryResponse>> getAllProductFields() {
        List<ProducingCountryResponse> products = producingCountryService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProducingCountryResponse> updateProductField(@RequestBody ProducingCountryRequest request, @PathVariable Long id) {
        ProducingCountryResponse updatedProductField = producingCountryService.update(request, id);
        return new ResponseEntity<>(updatedProductField, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductField(@PathVariable Long id) {
        producingCountryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
