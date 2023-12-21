package com.example.appliances.service.impl;

import com.example.appliances.entity.Product;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ProductMapper;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

     ProductRepository productRepository;
     ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    @Override
    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        Product product = productMapper.requestToEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.entityToResponse(savedProduct);
    }
    @Override
    @Transactional
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует!"));
        return productMapper.entityToResponse(product);
    }
    @Override
    @Transactional
    public ProductResponse update(ProductRequest productRequest, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует"));
        productMapper.update(product, productRequest);
        Product updatedProduct = productRepository.save(product);
        return productMapper.entityToResponse(updatedProduct);
    }
    @Override
    @Transactional
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует"));

        int updatedStock = product.getStock() + quantity;
        if (updatedStock < 0) {
            throw new RuntimeException("Недостаточно товара на складе");
        }

        product.setStock(updatedStock);
        productRepository.save(product);
    }
}