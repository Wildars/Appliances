package com.example.appliances.service.impl;

import com.example.appliances.entity.ProductField;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ProductFieldMapper;
import com.example.appliances.model.request.ProductFieldRequest;
import com.example.appliances.model.response.ProductFieldResponse;
import com.example.appliances.repository.ProductFieldRepository;
import com.example.appliances.service.ProductFieldService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductFieldServiceImpl implements ProductFieldService {

     ProductFieldRepository productFieldRepository;
     ProductFieldMapper productFieldMapper;

    @Autowired
    public ProductFieldServiceImpl(ProductFieldRepository productFieldRepository, ProductFieldMapper productFieldMapper) {
        this.productFieldRepository = productFieldRepository;
        this.productFieldMapper = productFieldMapper;
    }

    @Override
    public Page<ProductFieldResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy) {
        Pageable paging = PageRequest.of(page, size, sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return productFieldRepository.findAll(paging).map(productFieldMapper::entityToResponse);
    }

    @Override
    public ProductFieldResponse create(ProductFieldRequest productFieldRequest) {
        ProductField productField = productFieldMapper.requestToEntity(productFieldRequest);
        ProductField savedProductField = productFieldRepository.save(productField);
        return productFieldMapper.entityToResponse(savedProductField);
    }

    @Override
    public ProductFieldResponse findById(Long id) {
        ProductField productField = productFieldRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        return productFieldMapper.entityToResponse(productField);
    }

    @Override
    public ProductFieldResponse update(ProductFieldRequest productFieldRequest, Long id) {
        ProductField existingProductField = productFieldRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        productFieldMapper.update(existingProductField, productFieldRequest);
        ProductField updatedProductField = productFieldRepository.save(existingProductField);
        return productFieldMapper.entityToResponse(updatedProductField);
    }

    @Override
    public List<ProductFieldResponse> findAll() {
        List<ProductField> productFields = productFieldRepository.findAll();
        return productFields.stream().map(productFieldMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        productFieldRepository.deleteById(id);
    }
}
