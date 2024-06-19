package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SupplierMapper;
import com.example.appliances.mapper.SupplyItemMapper;
import com.example.appliances.mapper.SupplyMapper;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.SupplierRepository;
import com.example.appliances.repository.SupplyItemRepository;
import com.example.appliances.repository.SupplyRepository;
import com.example.appliances.repository.WishListRepository;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.StorageService;
import com.example.appliances.service.SupplierService;
import com.example.appliances.service.SupplyService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SupplierServiceImpl implements SupplierService {

    SupplyRepository supplyRepository;
    SupplyItemMapper supplyItemMapper;

    SupplyItemRepository supplyItemRepository;

    PasswordEncoder passwordEncoder;
    SupplierMapper supplierMapper;
    SupplierRepository supplierRepository;
    SupplyMapper supplyMapper;

    WishListMapper wishListMapper;

    WishListRepository wishListRepository;


    public SupplierServiceImpl(SupplyRepository supplyRepository, SupplyItemMapper supplyItemMapper, SupplyItemRepository supplyItemRepository, PasswordEncoder passwordEncoder, SupplyMapper supplyMapper, WishListMapper wishListMapper, WishListRepository wishListRepository, ProductService productService, StorageService storageService, SupplierMapper supplierMapper, SupplierRepository supplierRepository) {
        this.supplyRepository = supplyRepository;
        this.supplyItemMapper = supplyItemMapper;
        this.supplyItemRepository = supplyItemRepository;
        this.passwordEncoder = passwordEncoder;
        this.supplyMapper = supplyMapper;
        this.wishListMapper = wishListMapper;
        this.wishListRepository = wishListRepository;
        this.supplierMapper = supplierMapper;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public SupplierResponse findByUsername(String pin) {
        Supplier supplier = supplierRepository.findByPin(pin);
        if (supplier == null) {
            throw new RecordNotFoundException("Supplier with username " + pin + " not found");
        }
        return supplierMapper.entityToResponse(supplier);
    }

    @Override
    @Transactional
    public Page<SupplierResponse> getAllSuppliers(int page,
                                                  int size,
                                                  Optional<Boolean> sortOrder,
                                                  String sortBy) {
        Pageable paging = null;

        if (sortOrder.isPresent()) {
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Supplier> saleItemsPage = supplierRepository.findAll(paging);

        return saleItemsPage.map(supplierMapper::entityToResponse);
    }

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Supplier supply = supplierMapper.requestToEntity(request);
        supply.setPassword(passwordEncoder.encode(request.getPassword()));
        supply.setIsActive(true);
        Role role = new Role();
        role.setId(4L);
        supply.setRole(role);
        Supplier savedSupply = supplierRepository.save(supply);

        return supplierMapper.entityToResponse(savedSupply);
    }

    @Override
    @Transactional
    public SupplierResponse findById(Long id) {
        Supplier saleItem = supplierRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует!"));
        return supplierMapper.entityToResponse(saleItem);
    }

    @Override
    @Transactional
    public SupplierResponse update(SupplierRequest request, Long id) {
        Supplier saleItem = supplierRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует"));
        supplierMapper.update(saleItem, request);
        saleItem.setPassword(passwordEncoder.encode(request.getPassword()));
        Supplier updatedSaleItem = supplierRepository.save(saleItem);
        return supplierMapper.entityToResponse(updatedSaleItem);
    }

    @Override
    @Transactional
    public List<SupplierResponse> findAll() {
        List<Supplier> supplies = supplierRepository.findAll();
        return supplies.stream().map(supplierMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public boolean validateLogin(String pin, String password) {
        Supplier supplier = supplierRepository.findByPin(pin);
        if (supplier != null && passwordEncoder.matches(password, supplier.getPassword())) {
            return true;
        }
        return false;
    }
}
