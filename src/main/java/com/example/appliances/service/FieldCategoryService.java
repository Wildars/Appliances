package com.example.appliances.service;

import com.example.appliances.model.request.FieldRequest;
import com.example.appliances.model.response.FieldResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FieldCategoryService {

    public FieldResponse create(FieldRequest fieldRequest);
    public FieldResponse findById(Long id) ;

    public Page<FieldResponse> getAll(int page,
                                                               int size,
                                                               Optional<Boolean> sortOrder,
                                                               String sortBy);

    public FieldResponse update(FieldRequest fieldRequest, Long id);

    public List<FieldResponse> findAll();

    public void deleteById(Long id) ;
}
