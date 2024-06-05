package com.example.appliances.api;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Manager;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ManagerRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ManagerResponse;
import com.example.appliances.service.ClientService;
import com.example.appliances.service.ManagerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/managers")
public class ManagerApi {

    ManagerService managerService;

    public ManagerApi(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<ManagerResponse> create(@RequestBody ManagerRequest clientRequest) {
        ManagerResponse createdProduct = managerService.create(clientRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getById(@PathVariable Long id) {
        Manager product = managerService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/list")
    public Page<ManagerResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "25") int size,
                                                        @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                        @RequestParam(required = false) String sortBy) {
        return managerService.getAllClient(page, size, sortOrder, sortBy);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponse> update(@RequestBody ManagerRequest clientRequest, @PathVariable Long id) {
        ManagerResponse updatedProduct = managerService.update(clientRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAll() {
        List<ManagerResponse> products = managerService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        managerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
