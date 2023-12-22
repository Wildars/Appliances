package com.example.appliances.service.impl;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.DiscountCategory;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.SaleItem;
import com.example.appliances.exception.ProductNotAvailableException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ClientMapper;
import com.example.appliances.mapper.SaleItemMapper;
import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.repository.SaleItemRepository;
import com.example.appliances.service.ClientService;
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

    ClientService clientService;

    ClientMapper clientMapper;
    StorageService storageService;
    SaleItemRepository saleItemRepository;
    SaleItemMapper saleItemMapper;

    public SaleItemServiceImpl(ProductService productService, ClientService clientService, ClientMapper clientMapper, StorageService storageService, SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper) {
        this.productService = productService;
        this.clientService = clientService;
        this.clientMapper = clientMapper;
        this.storageService = storageService;
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
    }
    @Override
    @Transactional
    public SaleItemResponse create(SaleItemRequest saleItemRequest) {
        SaleItem saleItem = saleItemMapper.requestToEntity(saleItemRequest);
        // Очень надеюсь что комменты помогут понять тебе код
        // Получаю информацию о товаре из склада
        Product product = storageService.getProductById(saleItemRequest.getProductId());

        // Проверяю наличие товара на складе
        storageService.checkProductAvailability(product.getId(), saleItemRequest.getQuantity());

        // Обновляю количество товара на складе (уменьшаем)
        storageService.updateStockByProductId(product.getId(), saleItemRequest.getQuantity());

        // Получаю информацию о клиенте и его скидке
        Client client = clientService.findById(saleItemRequest.getClientId());

        // Присваиваю клиента к SaleItem
        saleItem.setClient(client);

        // Вычисляю totalPrice
        double totalPrice = product.getPrice() * saleItemRequest.getQuantity();
        saleItem.setTotalPrice(totalPrice);

        // Вычисляю итоговую стоимость с учетом скидки
        double totalPriceWithDiscount = calculateTotalPriceWithDiscount(saleItem);
        saleItem.setTotalPrice(totalPriceWithDiscount); // Использую setTotalPrice для сохранения с учетом скидки

        SaleItem savedSaleItem = saleItemRepository.save(saleItem);

        // Использую маппер для преобразования сущности SaleItem в SaleItemResponse
        return saleItemMapper.entityToResponse(savedSaleItem);
    }

    private double calculateTotalPriceWithDiscount(SaleItem saleItem) {
        Client client = saleItem.getClient();
        DiscountCategory discountCategory = client.getDiscountCategory();

        double discountPercentage = (discountCategory != null) ? discountCategory.getPercentage() : 0.0;
        double discount = saleItem.getTotalPrice() * (discountPercentage / 100.0);

        return saleItem.getTotalPrice() - discount;
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