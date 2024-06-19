package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.enums.ReturnStatusEnum;
import com.example.appliances.model.request.ReturnFilialRequest;
import com.example.appliances.model.request.ReturnItemFilialRequest;
import com.example.appliances.model.response.ReturnFilialResponse;
import com.example.appliances.repository.*;
import com.example.appliances.service.ReturnService;
import com.example.appliances.service.StorageItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReturnServiceImpl implements ReturnService {
    private final ReturnRepository returnRepository;
    private final ReturnItemRepository returnItemRepository;
    private final FilialRepository filialRepository;
    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;
    private final StorageItemService storageItemService;

    public ReturnServiceImpl(ReturnRepository returnRepository, ReturnItemRepository returnItemRepository,
                         FilialRepository filialRepository, StorageRepository storageRepository,
                         ProductRepository productRepository, StorageItemService storageItemService) {
        this.returnRepository = returnRepository;
        this.returnItemRepository = returnItemRepository;
        this.filialRepository = filialRepository;
        this.storageRepository = storageRepository;
        this.productRepository = productRepository;
        this.storageItemService = storageItemService;
    }

    @Transactional
    @Override
    public ReturnFilialResponse createReturn(ReturnFilialRequest request) {
        Filial fromFilial = filialRepository.findById(request.getFromFilialId())
                .orElseThrow(() -> new RuntimeException("Filial not found"));

        Storage toStorage = storageRepository.findById(request.getToStorageId())
                .orElseThrow(() -> new RuntimeException("Storage not found"));

        // Создание записи в ReturnFilial
        ReturnFilial returnEntity = ReturnFilial.builder()
                .fromFilial(fromFilial)
                .toStorage(toStorage)
                .status(ReturnStatusEnum.SENT)
                .returnDate(LocalDateTime.now())
                .build();

        returnEntity = returnRepository.save(returnEntity);

        // Обработка каждого товара
        for (ReturnItemFilialRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Обновление количества товара только на филиале
            storageItemService.updateStockInFilial(
                    itemRequest.getProductId(),
                    fromFilial.getId(),
                    itemRequest.getQuantity()
            );

            // Создание записи в ReturnFilialItem
            ReturnFilialItem returnItem = ReturnFilialItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .returnFilial(returnEntity)
                    .build();

            returnItemRepository.save(returnItem);
        }

        // Создание и возврат ответа
        ReturnFilialResponse response = ReturnFilialResponse.builder()
                .returnId(returnEntity.getId())
                .status(returnEntity.getStatus())
                .build();

        return response;
    }


    @Transactional
    @Override
    public void acceptReturn(Long returnId) {
        ReturnFilial returnEntity = returnRepository.findById(returnId)
                .orElseThrow(() -> new RuntimeException("Return not found"));

        if (returnEntity.getStatus() != ReturnStatusEnum.SENT) {
            throw new IllegalStateException("Return must be in SENT status to be accepted");
        }

        for (ReturnFilialItem item : returnEntity.getReturnFilialItems()) {
            storageItemService.updateStockByProductId(item.getProduct().getId(), returnEntity.getToStorage().getId(), -item.getQuantity());
        }

        returnEntity.setStatus(ReturnStatusEnum.ACCEPTED);
        returnRepository.save(returnEntity);
    }

    @Override
    @Transactional
    public void revokeReturn(Long returnId) {
        ReturnFilial returnEntity = returnRepository.findById(returnId)
                .orElseThrow(() -> new RuntimeException("Return not found"));

        if (returnEntity.getStatus() != ReturnStatusEnum.SENT) {
            throw new IllegalStateException("Return must be in SENT status to be revoked");
        }

        for (ReturnFilialItem item : returnEntity.getReturnFilialItems()) {
            // Обновляем количество товара на филиале
            storageItemService.updateStockInFilial(item.getProduct().getId(), returnEntity.getFromFilial().getId(), -item.getQuantity());
        }

        returnEntity.setStatus(ReturnStatusEnum.REVOKED);
        returnRepository.save(returnEntity);
    }

    @Override
    @Transactional
    public void refuseReturn(Long returnId) {
        ReturnFilial returnEntity = returnRepository.findById(returnId)
                .orElseThrow(() -> new RuntimeException("Return not found"));

        if (returnEntity.getStatus() != ReturnStatusEnum.SENT) {
            throw new IllegalStateException("Return must be in SENT status to be refused");
        }

        for (ReturnFilialItem item : returnEntity.getReturnFilialItems()) {
            // Обновляем количество товара на филиале
            storageItemService.updateStockInFilial(item.getProduct().getId(), returnEntity.getFromFilial().getId(), -item.getQuantity());
        }

        returnEntity.setStatus(ReturnStatusEnum.REFUSED);
        returnRepository.save(returnEntity);
    }
}