package com.example.appliances.service.impl;

import com.example.appliances.entity.Storage;
import com.example.appliances.entity.Supply;
import com.example.appliances.entity.SupplyItem;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SupplyItemMapper;
import com.example.appliances.mapper.SupplyMapper;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.repository.SupplyItemRepository;
import com.example.appliances.repository.SupplyRepository;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.SupplyService;
import com.example.appliances.service.StorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SupplyServiceImpl implements SupplyService {

    SupplyRepository supplyRepository;
    SupplyItemMapper supplyItemMapper;

    SupplyItemRepository supplyItemRepository;
    SupplyMapper supplyMapper;


ProductService productService;
    StorageService storageService;

    public SupplyServiceImpl(SupplyRepository supplyRepository, SupplyItemMapper supplyItemMapper, SupplyItemRepository supplyItemRepository, SupplyMapper supplyMapper, ProductService productService, StorageService storageService) {
        this.supplyRepository = supplyRepository;
        this.supplyItemMapper = supplyItemMapper;
        this.supplyItemRepository = supplyItemRepository;
        this.supplyMapper = supplyMapper;
        this.productService = productService;
        this.storageService = storageService;
    }
    @Override
    @Transactional
    public List<Supply> findAlls() {
        return supplyRepository.findAll();
    }

    @Override
    @Transactional
    public Page<SupplyResponse> getAllSuppliers(int page,
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
        Page<Supply> saleItemsPage = supplyRepository.findAll(paging);

        return saleItemsPage.map(supplyMapper::entityToResponse);
    }
    @Override
    @Transactional
    public SupplyResponse create(SupplyRequest supplyRequest) {
        Supply supply = supplyMapper.requestToEntity(supplyRequest);

        List<SupplyItem> supplyItems = new ArrayList<>();

        for (SupplyItemRequest itemRequest : supplyRequest.getSupplyItems()) {
            SupplyItem supplyItem = supplyItemMapper.requestToEntity(itemRequest);
//            supplyItem.setSupply(supply);

            supplyItems.add(supplyItem);

            // Обновляю количество товара на складе
            storageService.updateStock(itemRequest.getProductId(), supplyRequest.getStorageId(), itemRequest.getQuantity());
        }

        supply.setSupplyItems(supplyItems);

        Supply savedSupply = supplyRepository.save(supply);

        return supplyMapper.entityToResponse(savedSupply);
    }
    @Override
    @Transactional
    public SupplyResponse findById(Long id) {
        Supply saleItem = supplyRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует!"));
        return supplyMapper.entityToResponse(saleItem);
    }
    @Override
    @Transactional
    public SupplyResponse update(SupplyRequest supplyRequest, Long id) {
        Supply saleItem = supplyRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует"));
        supplyMapper.update(saleItem, supplyRequest);
        Supply updatedSaleItem = supplyRepository.save(saleItem);
        return supplyMapper.entityToResponse(updatedSaleItem);
    }
    @Override
    @Transactional
    public List<SupplyItemResponse> findAll() {
        List<SupplyItem> supplies = supplyItemRepository.findAll();
        return supplies.stream().map(supplyItemMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        supplyRepository.deleteById(id);
    }
}
