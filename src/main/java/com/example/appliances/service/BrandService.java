package com.example.appliances.service;

import com.example.appliances.model.request.BrandRequest;
import com.example.appliances.model.response.BrandResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    Page<BrandResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy);
    BrandResponse create(BrandRequest request);
    BrandResponse findById(Long id);
    BrandResponse update(BrandRequest request, Long id);
    List<BrandResponse> findAll();
    void deleteById(Long id);
}
