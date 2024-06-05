package com.example.appliances.api;

import com.example.appliances.model.request.FieldRequest;
import com.example.appliances.model.response.FieldResponse;
import com.example.appliances.service.FieldCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/field")
public class FieldApi {

    private final FieldCategoryService fieldCategoryService;
    @Autowired
    public FieldApi(FieldCategoryService fieldCategoryService) {
        this.fieldCategoryService = fieldCategoryService;
    }




    @GetMapping("/page")
    public Page<FieldResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "25") int size,
                                                      @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                      @RequestParam(required = false) String sortBy) {
        return fieldCategoryService.getAll(page, size, sortOrder, sortBy);
    }
    @PostMapping("/create")
    public ResponseEntity<FieldResponse> create(@RequestBody FieldRequest fieldRequest) {
        FieldResponse createdProduct = fieldCategoryService.create(fieldRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponse> getById(@PathVariable Long id) {
        FieldResponse product = fieldCategoryService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldResponse> update(@RequestBody FieldRequest fieldRequest, @PathVariable Long id) {
        FieldResponse updatedProduct = fieldCategoryService.update(fieldRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FieldResponse>> getAll() {
        List<FieldResponse> products = fieldCategoryService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fieldCategoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}