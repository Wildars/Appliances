package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.TransferItemMapper;
import com.example.appliances.mapper.TransferMapper;
import com.example.appliances.model.request.TransferRequest;
import com.example.appliances.model.response.ProductFieldResponse;
import com.example.appliances.model.response.TransferItemResponse;
import com.example.appliances.model.response.TransferResponse;
import com.example.appliances.repository.*;
import com.example.appliances.service.FilialItemService;
import com.example.appliances.service.StorageItemService;
import com.example.appliances.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

    private final StorageItemService storageItemService;
    private final FilialItemService filialItemService;
    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;
    private final ProductRepository productRepository;
    private final FilialRepository filialRepository;
    private final StorageRepository storageRepository;
    private final TransferItemMapper transferItemMapper;
    private final TransferMapper transferMapper;

    public TransferServiceImpl(StorageItemService storageItemService, FilialItemService filialItemService,
                               TransferRepository transferRepository, TransferItemRepository transferItemRepository,
                               ProductRepository productRepository, FilialRepository filialRepository,
                               StorageRepository storageRepository, TransferItemMapper transferItemMapper, TransferMapper transferMapper) {
        this.storageItemService = storageItemService;
        this.filialItemService = filialItemService;
        this.transferRepository = transferRepository;
        this.transferItemRepository = transferItemRepository;
        this.productRepository = productRepository;
        this.filialRepository = filialRepository;
        this.storageRepository = storageRepository;
        this.transferItemMapper = transferItemMapper;
        this.transferMapper = transferMapper;
    }

    @Override
    @Transactional
    public void transferProducts(List<TransferRequest> transferRequests) {
        for (TransferRequest request : transferRequests) {
            UUID productId = request.getProductId();
            Long storageId = request.getStorageId();
            Long filialId = request.getFilialId();
            int quantity = request.getQuantity();

            // Проверка доступности товара на складе
            storageItemService.checkProductAvailability(productId, quantity);

            // Уменьшение количества товара на складе
            storageItemService.updateStockByProductId(productId, quantity);

            // Получение объектов Product, Storage и Filial из репозиториев
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            Filial filial = filialRepository.findById(filialId)
                    .orElseThrow(() -> new IllegalArgumentException("Filial not found"));
            Storage storage = storageRepository.findById(storageId)
                    .orElseThrow(() -> new IllegalArgumentException("Storage not found"));

            // Создание объекта Transfer и его сохранение в базе данных
            Transfer transfer = new Transfer();
            transfer.setTransferDate(LocalDateTime.now());
            transfer.setFromStorage(storage);
            transfer.setToFilial(filial);
            Transfer savedTransfer = transferRepository.save(transfer);

            // Создание объекта TransferItem и его сохранение в базе данных
            TransferItem transferItem = new TransferItem();
            transferItem.setProduct(product);
            transferItem.setQuantity(quantity);
            transferItem.setTransfer(savedTransfer);
            transferItemRepository.save(transferItem);

            // Обновление количества товара в филиале
            FilialItem filialItem = filialItemService.findByProductIdAndFilialId(productId, filialId);
            if (filialItem != null) {
                // Если товар уже есть в филиале, увеличиваем количество
                filialItem.setQuantity(filialItem.getQuantity() + quantity);
            } else {
                // Если товара нет в филиале, создаем новую запись
                filialItem = new FilialItem();
                filialItem.setProduct(product);
                filialItem.setFilial(filial);
                filialItem.setQuantity(quantity);
            }
            filialItemService.create(filialItem);
        }
    }




    @Override
    @Transactional
    public List<TransferResponse> findAllTransfers() {
        List<Transfer> productFields = transferRepository.findAll();
        return productFields.stream().map(transferMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransferResponse findByIdransfers(Long id) {
        Transfer productField = transferRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        return transferMapper.entityToResponse(productField);
    }

    @Override
    @Transactional
    public List<TransferItemResponse> findAllransfersItem() {
        List<TransferItem> productFields = transferItemRepository.findAll();
        return productFields.stream().map(transferItemMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransferItemResponse findByIdransfersItem(Long id) {
        TransferItem productField = transferItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product field not found with id: " + id));
        return transferItemMapper.entityToResponse(productField);
    }
}
