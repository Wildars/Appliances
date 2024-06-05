package com.example.appliances.service;

import com.example.appliances.entity.Product;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

     public ProductResponse create(ProductRequest productRequest, List<MultipartFile> photos) throws IOException;


     public Resource getProductImage(UUID productId) throws IOException;

     public Resource getImageByName(String photoName) throws IOException;


     public ProductResponse getProductById(UUID id);

     public Page<ProductResponse> getAllProduct(int page,
                                                int size,
                                                Optional<Boolean> sortOrder,
                                                String sortBy);


     public void updateStock(UUID productId, int quantity) ;
     ProductResponse update(ProductRequest productRequest, UUID productId);

     public Page<ProductResponse> getProductsByCategoryIdAndFilters(Long categoryId, List<Long> brandIds, List<Long> producingCountryIds, BigDecimal minPrice, BigDecimal maxPrice, int page, int size);

     List<ProductResponse> findAll();

     public List<ProductResponse> findAllProduct();

     void deleteById(UUID id) ;

     Product getById(UUID id);

     public Long countAllProducts();
}
