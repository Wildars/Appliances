package com.example.appliances.service.impl;

import com.example.appliances.entity.Supply;
import com.example.appliances.entity.SupplyItem;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SupplyItemMapper;
import com.example.appliances.mapper.SupplyMapper;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.repository.SupplyItemRepository;
import com.example.appliances.service.SupplyItemService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
public class SupplyItemServiceImpl implements SupplyItemService {

    SupplyItemRepository supplyItemRepository;

    SupplyItemMapper supplyItemMapperu;

    public SupplyItemServiceImpl(SupplyItemRepository supplyItemRepository, SupplyItemMapper supplyItemMapperu) {
        this.supplyItemRepository = supplyItemRepository;
        this.supplyItemMapperu = supplyItemMapperu;
    }
    @Override
    @Transactional
    public SupplyItemResponse create(SupplyItemRequest supplyItemRequest) {
//        SupplyItem saleItem = supplyItemMapperu.requestToEntity(supplyItemRequest);
//
//        for (SupplyItemRequest itemRequest : supplyItemRequest.getSupplyItems()) {
//            SupplyItem supplyItem = supplyItemMapper.requestToEntity(itemRequest);
//            // supplyItem.setSupply(supply);
//
//            supplyItems.add(supplyItem);
//
//            // Обновляем количество товара на складе
//            storageService.updateStock(itemRequest.getProductId(), supplyRequest.getStorageId(), itemRequest.getQuantity());
//        }
//        SupplyItem savedSaleItem = supplyItemRepository.save(saleItem);
//
//
//        return supplyItemMapperu.entityToResponse(savedSaleItem);
        return null;
    }
    @Override
    @Transactional
    public SupplyItemResponse findById(Long id) {
        SupplyItem saleItem = supplyItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует!"));
        return supplyItemMapperu.entityToResponse(saleItem);
    }
    @Override
    @Transactional
    public SupplyItemResponse update(SupplyItemRequest supplyItemRequest, Long id) {
        SupplyItem saleItem = supplyItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Поставщика с таким id не существует"));
        supplyItemMapperu.update(saleItem, supplyItemRequest);
        SupplyItem updatedSaleItem = supplyItemRepository.save(saleItem);
        return supplyItemMapperu.entityToResponse(updatedSaleItem);
    }
    @Override
    @Transactional
    public List<SupplyItemResponse> findAll() {
        List<SupplyItem> saleItems = supplyItemRepository.findAll();
        return saleItems.stream().map(supplyItemMapperu::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        supplyItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Page<SupplyItemResponse> getAllSupplierItemPages(int page,
                                                        int size,
                                                        Optional<Boolean> sortOrder,
                                                        String sortBy){
        Pageable paging = null;

        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<SupplyItem> saleItemsPage = supplyItemRepository.findAll(paging);

        return saleItemsPage.map(supplyItemMapperu::entityToResponse);
    }

}