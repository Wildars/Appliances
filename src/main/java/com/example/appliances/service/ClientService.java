package com.example.appliances.service;

import com.example.appliances.entity.Client;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    public ClientResponse create(ClientRequest clientRequest);

    public Client findById(Long id) ;

    public Page<ClientResponse> getAllClient(int page,
                                             int size,
                                             Optional<Boolean> sortOrder,
                                             String sortBy);

    public ClientResponse update(ClientRequest clientRequest, Long id);

    public List<ClientResponse> findAll();

    public void deleteById(Long id) ;
}