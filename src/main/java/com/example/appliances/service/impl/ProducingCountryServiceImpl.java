package com.example.appliances.service.impl;

import com.example.appliances.entity.ProducingCountry;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ProducingCountryMapper;
import com.example.appliances.model.request.ProducingCountryRequest;
import com.example.appliances.model.response.ProducingCountryResponse;
import com.example.appliances.repository.ProducingCountryRepository;
import com.example.appliances.service.ProducingCountryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProducingCountryServiceImpl implements ProducingCountryService {

    ProducingCountryMapper producingCountryMapper;

    ProducingCountryRepository producingCountryRepository;

    public ProducingCountryServiceImpl(ProducingCountryMapper producingCountryMapper, ProducingCountryRepository producingCountryRepository) {
        this.producingCountryMapper = producingCountryMapper;
        this.producingCountryRepository = producingCountryRepository;
    }

    @Override
    public Page<ProducingCountryResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy) {
        Pageable paging = PageRequest.of(page, size, sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return producingCountryRepository.findAll(paging).map(producingCountryMapper::entityToResponse);
    }

    @Override
    public ProducingCountryResponse create(ProducingCountryRequest request) {
        ProducingCountry producingCountry = producingCountryMapper.requestToEntity(request);
        ProducingCountry save = producingCountryRepository.save(producingCountry);
        return producingCountryMapper.entityToResponse(save);
    }

    @Override
    public ProducingCountryResponse findById(Long id) {
        ProducingCountry producingCountry = producingCountryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        return producingCountryMapper.entityToResponse(producingCountry);
    }

    @Override
    public ProducingCountryResponse update(ProducingCountryRequest request, Long id) {
        ProducingCountry existingCountry = producingCountryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        producingCountryMapper.update(existingCountry, request);
        ProducingCountry updateCountry = producingCountryRepository.save(existingCountry);
        return producingCountryMapper.entityToResponse(updateCountry);
    }

    @Override
    public List<ProducingCountryResponse> findAll() {
        List<ProducingCountry> producingCountries = producingCountryRepository.findAll();
        return producingCountries.stream().map(producingCountryMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        producingCountryRepository.deleteById(id);
    }
}
