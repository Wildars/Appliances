package com.example.appliances.service;

import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
     SupplierResponse create(SupplierRequest request);

     SupplierResponse findById(Long id);

     SupplierResponse update(SupplierRequest request, Long id);

     List<SupplierResponse> findAll();

     public Page<SupplierResponse> getAllSuppliers(int page,
                                                 int size,
                                                 Optional<Boolean> sortOrder,
                                                 String sortBy);


     public SupplierResponse findByUsername(String username);
     public boolean validateLogin(String pin, String password);
     void deleteById(Long id);
}
