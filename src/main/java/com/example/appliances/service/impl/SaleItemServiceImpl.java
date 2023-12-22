package com.example.appliances.service.impl;

import com.example.appliances.entity.Product;
import com.example.appliances.entity.SaleItem;
import com.example.appliances.exception.ProductNotAvailableException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SaleItemMapper;
import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.repository.SaleItemRepository;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.SaleItemService;
import com.example.appliances.service.StorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SaleItemServiceImpl implements SaleItemService {
    ProductService productService;

    StorageService storageService;
    SaleItemRepository saleItemRepository;
    SaleItemMapper saleItemMapper;

    public SaleItemServiceImpl(ProductService productService, StorageService storageService, SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper) {
        this.productService = productService;
        this.storageService = storageService;
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
    }
    @Override
    @Transactional
    public SaleItemResponse create(SaleItemRequest saleItemRequest) {
        SaleItem saleItem = saleItemMapper.requestToEntity(saleItemRequest);

        // Получаем информацию о товаре из склада
        Product product = storageService.getProductById(saleItemRequest.getProductId());

        // Проверяем наличие товара на складе
        storageService.checkProductAvailability(product.getId(), saleItemRequest.getQuantity());

        // Обновляем количество товара на складе (уменьшаем)
        storageService.updateStockByProductId(product.getId(), saleItemRequest.getQuantity());

        // Вычисляем totalPrice
        double totalPrice = product.getPrice() * saleItemRequest.getQuantity();
        saleItem.setTotalPrice(totalPrice);

        SaleItem savedSaleItem = saleItemRepository.save(saleItem);
        return saleItemMapper.entityToResponse(savedSaleItem);
    }

    private void checkProductAvailability(Long productId, int quantity) {
        int availableQuantity = storageService.getAvailableQuantity(productId);
        if (quantity > availableQuantity) {
            throw new ProductNotAvailableException("Недостаточное количество товара на складе");
        }
    }
    @Override
    @Transactional
    public SaleItemResponse findById(Long id) {
        SaleItem saleItem = saleItemRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Элемент продажи с таким id не существует!"));
        return saleItemMapper.entityToResponse(saleItem);
    }
    @Override
    @Transactional
    public SaleItemResponse update(SaleItemRequest saleItemRequest, Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId)
                .orElseThrow(() -> new RecordNotFoundException("Элемент продажи с таким id не существует"));
        saleItemMapper.update(saleItem, saleItemRequest);
        SaleItem updatedSaleItem = saleItemRepository.save(saleItem);
        return saleItemMapper.entityToResponse(updatedSaleItem);
    }
    @Override
    @Transactional
    public List<SaleItemResponse> findAll() {
        List<SaleItem> saleItems = saleItemRepository.findAll();
        return saleItems.stream().map(saleItemMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        saleItemRepository.deleteById(id);
    }
}