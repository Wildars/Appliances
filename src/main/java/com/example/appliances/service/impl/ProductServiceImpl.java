package com.example.appliances.service.impl;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ProductMapper;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public Page<ProductResponse> getAllProduct(int page,
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
        Page<Product> saleItemsPage = productRepository.findAll(paging);

        return saleItemsPage.map(productMapper::entityToResponse);
    }


    @Override
    public List<ProductResponse> findAllProduct() {
        return productMapper.toResponseList(productRepository.findAll());
    }
    @Transactional
    @Override
    public ProductResponse create(ProductRequest productRequest, List<MultipartFile> photos) throws IOException {
        Product product = productMapper.requestToEntity(productRequest, photos);
        List<String> photoPaths = MediaTypeUtils.saveImages(photos);
        product.setPhotoPaths(photoPaths);
        Product savedProduct = productRepository.save(product);
        return productMapper.entityToResponse(savedProduct);
    }
    @Override
    @Transactional
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует!"));
        return productMapper.entityToResponse(product);
    }
    @Override
    @Transactional
    public ProductResponse update(ProductRequest productRequest, UUID productId) {
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
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStock(UUID productId, int quantity) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует"));
//
//        int updatedStock = product.getStock() + quantity;
//        if (updatedStock < 0) {
//            throw new RuntimeException("Недостаточно товара на складе");
//        }
//
//        product.setStock(updatedStock);
//        productRepository.save(product);
    }

    @Override
    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product not found with id: " + id));
    }
}