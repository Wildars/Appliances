package com.example.appliances.api;

import com.example.appliances.entity.Client;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.service.ClientService;
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
@RequestMapping("/clients")
public class ClientApi {
    ClientService clientService;

    public ClientApi(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest clientRequest) {
        ClientResponse createdProduct = clientService.create(clientRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client product = clientService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/list")
    public Page<ClientResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "25") int size,
                                                        @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                        @RequestParam(required = false) String sortBy) {
        return clientService.getAllClient(page, size, sortOrder, sortBy);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@RequestBody ClientRequest clientRequest, @PathVariable Long id) {
        ClientResponse updatedProduct = clientService.update(clientRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> products = clientService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
