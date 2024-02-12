package com.example.appliances.service;

import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     ProductResponse create(ProductRequest productRequest);

     ProductResponse getProductById(Long id) ;

     public Page<ProductResponse> getAllProduct(int page,
                                                int size,
                                                Optional<Boolean> sortOrder,
                                                String sortBy);


     public void updateStock(Long productId, int quantity) ;
     ProductResponse update(ProductRequest productRequest, Long productId);

     List<ProductResponse> findAll();

     public List<ProductResponse> findAllProduct();

     void deleteById(Long id) ;
}
