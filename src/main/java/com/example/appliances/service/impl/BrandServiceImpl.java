package com.example.appliances.service.impl;

import com.example.appliances.entity.Brand;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.BrandMapper;
import com.example.appliances.model.request.BrandRequest;
import com.example.appliances.model.response.BrandResponse;
import com.example.appliances.repository.BrandRepository;
import com.example.appliances.service.BrandService;
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
public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;

    BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public Page<BrandResponse> getAll(int page, int size, Optional<Boolean> sortOrder, String sortBy) {
        Pageable paging = PageRequest.of(page, size, sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return brandRepository.findAll(paging).map(brandMapper::entityToResponse);
    }

    @Override
    public BrandResponse create(BrandRequest request) {
        Brand brand = brandMapper.requestToEntity(request);
        Brand save = brandRepository.save(brand);
        return brandMapper.entityToResponse(save);
    }

    @Override
    public BrandResponse findById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Brand field not found with id: " + id));
        return brandMapper.entityToResponse(brand);
    }

    @Override
    public BrandResponse update(BrandRequest request, Long id) {
        Brand existingBrand =  brandRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Brand field not found with id: " + id));
        brandMapper.update(existingBrand, request);
        Brand updatedBrand = brandRepository.save(existingBrand);
        return brandMapper.entityToResponse(updatedBrand);
    }

    @Override
    public List<BrandResponse> findAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(brandMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);

    }
}
