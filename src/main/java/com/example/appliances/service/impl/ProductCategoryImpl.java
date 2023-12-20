package com.example.appliances.service.impl;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ProductCategoryMapper;
import com.example.appliances.mapper.ProductMapper;
import com.example.appliances.model.request.ProductCategoryRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.repository.ProductCategoryRepository;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.service.ProductCategoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductCategoryImpl implements ProductCategoryService {
    ProductCategoryRepository productCategoryRepository;
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    public ProductCategoryImpl(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }
    @Override
    @Transactional
    public ProductCategoryResponse create(ProductCategoryRequest productRequest) {
        ProductCategory product = productCategoryMapper.requestToEntity(productRequest);
        ProductCategory savedProduct = productCategoryRepository.save(product);
        return productCategoryMapper.entityToResponse(savedProduct);
    }
    @Override
    @Transactional
    public ProductCategoryResponse findById(Long id) {
        ProductCategory product = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует!"));
        return productCategoryMapper.entityToResponse(product);
    }
    @Override
    @Transactional
    public ProductCategoryResponse update(ProductCategoryRequest productRequest, Long productId) {
        ProductCategory product = productCategoryRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Категория товара с таким id не существует"));
        productCategoryMapper.update(product, productRequest);
        ProductCategory updatedProduct = productCategoryRepository.save(product);
        return productCategoryMapper.entityToResponse(updatedProduct);
    }
    @Override
    @Transactional
    public List<ProductCategoryResponse> findAll() {
        List<ProductCategory> products = productCategoryRepository.findAll();
        return products.stream().map(productCategoryMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        productCategoryRepository.deleteById(id);
    }
}