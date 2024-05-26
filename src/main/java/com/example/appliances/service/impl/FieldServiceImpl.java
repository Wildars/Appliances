package com.example.appliances.service.impl;

import com.example.appliances.entity.Field;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.FieldMapper;
import com.example.appliances.model.request.FieldRequest;
import com.example.appliances.model.response.FieldResponse;
import com.example.appliances.repository.FieldRepository;
import com.example.appliances.service.FieldCategoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FieldServiceImpl implements FieldCategoryService {
    FieldRepository fieldRepository;

    FieldMapper fieldMapper;
    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
    }
    @Override
    public Page<FieldResponse> getAll(int page,
                                                     int size,
                                                     Optional<Boolean> sortOrder,
                                                     String sortBy) {
        Pageable paging = null;

        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Field> saleItemsPage = fieldRepository.findAll(paging);

        return saleItemsPage.map(fieldMapper::entityToResponse);
    }
    @Override
    public FieldResponse create(FieldRequest fieldRequest) {
        Field field = fieldMapper.requestToEntity(fieldRequest);
        Field save = fieldRepository.save(field);
        return fieldMapper.entityToResponse(save);

    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "productCategories", key = "#id")
    public FieldResponse findById(Long id) {
        Field product = fieldRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует!"));
        return fieldMapper.entityToResponse(product);
    }

    @Override
    @Transactional
    public FieldResponse update(FieldRequest fieldRequest, Long id) {
        Field product = fieldRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует"));
        fieldMapper.update(product, fieldRequest);
        Field updatedProduct = fieldRepository.save(product);
        return fieldMapper.entityToResponse(updatedProduct);
    }
    @Override
    @Transactional(readOnly = true)
    public List<FieldResponse> findAll() {
        List<Field> products = fieldRepository.findAll();
        return products.stream().map(fieldMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        fieldRepository.deleteById(id);
    }
}