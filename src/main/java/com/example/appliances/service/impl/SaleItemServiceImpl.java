package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.enums.SaleStatusEnum;
import com.example.appliances.exception.*;
import com.example.appliances.mapper.ClientMapper;
import com.example.appliances.mapper.SaleItemMapper;
import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.request.SaleItemRequest;
import com.example.appliances.model.response.SaleItemResponse;
import com.example.appliances.repository.ProductRepository;
import com.example.appliances.repository.SaleItemRepository;
import com.example.appliances.repository.SaleStatusRepository;
import com.example.appliances.service.ClientService;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.SaleItemService;
import com.example.appliances.service.StorageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SaleItemServiceImpl implements SaleItemService {
    ProductService productService;

    ProductRepository productRepository;
    SaleStatusRepository saleStatusRepository;
    ClientService clientService;



    ClientMapper clientMapper;
    StorageService storageService;
    SaleItemRepository saleItemRepository;
    SaleItemMapper saleItemMapper;

    public SaleItemServiceImpl(ProductService productService, ProductRepository productRepository, SaleStatusRepository saleStatusRepository, ClientService clientService, ClientMapper clientMapper, StorageService storageService, SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.saleStatusRepository = saleStatusRepository;
        this.clientService = clientService;
        this.clientMapper = clientMapper;
        this.storageService = storageService;
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
    }


    @Override
    public Page<SaleItemResponse> getAllSaleItems(int page,
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
        Page<SaleItem> saleItemsPage = saleItemRepository.findAll(paging);

        return saleItemsPage.map(saleItemMapper::entityToResponse);
    }

//    private Pageable createPageable(int page, int size, Optional<Boolean> sortOrder, String sortBy) {
//        Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
//        return PageRequest.of(page, size, direction, sortBy);
//    }





    @Override
    @Transactional
    public void sendSaleItem(Long saleItemId, SaleItemElementRequest request) {

        updateQueueEntryStatus(saleItemId, SaleStatusEnum.SENDET, null);
    }
    @Override
    @Transactional
    public void doneSaleItem(Long saleItemId, SaleItemElementRequest request) {

        updateQueueEntryStatus(saleItemId, SaleStatusEnum.DONE, null);
    }
    @Override
    @Transactional
    public void rejectSaleItem(Long queueEntryId, SaleItemElementRequest request) {

        SaleItem saleItem = saleItemRepository.findById(queueEntryId)
                .orElseThrow(() -> new SaleItemNotFoundException("Покупка с ID " + queueEntryId + " не найдена"));

        // Получить информацию о товаре, который был куплен
        List<Product> products = saleItem.getProducts();

        // Итерируем по товарам и увеличиваем количество каждого товара на складе
        for (Product product : products) {
            storageService.returnStockByProductId(product.getId(), saleItem.getQuantity());
        }

        // Обновить статус покупки на "REJECTED" и добавить комментарии
        updateQueueEntryStatus(queueEntryId, SaleStatusEnum.REJECTED, request.getComments());
    }

    private void updateQueueEntryStatus(Long queueEntryId, SaleStatusEnum status, String description) {
        SaleItem queueEntry = saleItemRepository.findById(queueEntryId)
                .orElseThrow(() -> new CustomException(CustomError.ENTITY_NOT_FOUND));

        SaleStatus queueEntryStatus = saleStatusRepository.findById(status.getId())
                .orElseThrow(() -> new EntityNotFoundException("QueueEntryStatus with id " + status.getId() + " not found"));

        queueEntry.setSaleStatus(queueEntryStatus);

        if (status == SaleStatusEnum.REJECTED) {
            queueEntry.setComments(description);
        }

        saleItemRepository.save(queueEntry);
    }

    @Override
    @Transactional
    public SaleItemResponse create(SaleItemRequest saleItemRequest) {
        SaleItem saleItem = saleItemMapper.requestToEntity(saleItemRequest);

        // Присваиваю статусы
        SaleStatus saleStatus = saleStatusRepository.findById(SaleStatusEnum.ACCEPTED.getId()).orElseThrow(() -> new RuntimeException("SaleStatus not found"));
        saleItem.setSaleStatus(saleStatus);

        // Получаю информацию о товаре из склада
        List<Product> products = storageService.getProductsById(saleItemRequest.getProductIds());

        // Проверяю наличие товара на складе
        for (Product product : products) {
            storageService.checkProductAvailability(product.getId(), saleItemRequest.getQuantity());
            // Обновляю количество товара на складе (уменьшаем)
            storageService.updateStockByProductId(product.getId(), saleItemRequest.getQuantity());
        }

        // Получаю информацию о клиенте и его скидке
        Client client = clientService.findById(saleItemRequest.getClientId());

        // Присваиваю клиента к SaleItem
        saleItem.setClient(client);

        // Вычисляю totalPrice
        double totalPrice = products.stream()
                .mapToDouble(product -> product.getPrice() * saleItemRequest.getQuantity())
                .sum();
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