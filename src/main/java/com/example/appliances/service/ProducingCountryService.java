package com.example.appliances.service;

import com.example.appliances.model.request.ProducingCountryRequest;
import com.example.appliances.model.response.ProducingCountryResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProducingCountryService {

    Page<ProducingCountryResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy);
    ProducingCountryResponse create(ProducingCountryRequest request);
    ProducingCountryResponse findById(Long id);
    ProducingCountryResponse update(ProducingCountryRequest request, Long id);
    List<ProducingCountryResponse> findAll();
    void deleteById(Long id);
}
