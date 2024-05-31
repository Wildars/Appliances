package com.example.appliances.service;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Manager;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ManagerRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ManagerResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    public ManagerResponse create(ManagerRequest request);

    public Manager findById(Long id) ;

    public Page<ManagerResponse> getAllClient(int page,
                                             int size,
                                             Optional<Boolean> sortOrder,
                                             String sortBy);

    public ManagerResponse update(ManagerRequest request, Long id);

    public List<ManagerResponse> findAll();

    public void deleteById(Long id) ;
}