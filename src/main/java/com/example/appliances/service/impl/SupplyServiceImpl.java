package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.enums.SupplyStatus;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SupplyItemMapper;
import com.example.appliances.mapper.SupplyMapper;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.StorageItemRequest;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.*;
import com.example.appliances.service.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SupplyServiceImpl implements SupplyService {

    StorageItemRepository storageItemRepository;
    SupplyRepository supplyRepository;
    SupplyItemMapper supplyItemMapper;

    ProductRepository productRepository;
    StorageRepository storageRepository;
    SupplierRepository supplierRepository;
    SupplyItemRepository supplyItemRepository;
    SupplyMapper supplyMapper;
    StorageItemService storageItemService;
    WishListMapper wishListMapper;

    WishListRepository wishListRepository;
ProductService productService;

    SupplierService supplierService;
    StorageService storageService;

    public SupplyServiceImpl(StorageItemRepository storageItemRepository, SupplyRepository supplyRepository, SupplyItemMapper supplyItemMapper, ProductRepository productRepository, StorageRepository storageRepository, SupplierRepository supplierRepository, SupplyItemRepository supplyItemRepository, SupplyMapper supplyMapper, StorageItemService storageItemService, WishListMapper wishListMapper, WishListRepository wishListRepository, ProductService productService, SupplierService supplierService, StorageService storageService) {
        this.storageItemRepository = storageItemRepository;
        this.supplyRepository = supplyRepository;
        this.supplyItemMapper = supplyItemMapper;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.supplierRepository = supplierRepository;
        this.supplyItemRepository = supplyItemRepository;
        this.supplyMapper = supplyMapper;
        this.storageItemService = storageItemService;
        this.wishListMapper = wishListMapper;
        this.wishListRepository = wishListRepository;
        this.productService = productService;
        this.supplierService = supplierService;
        this.storageService = storageService;
    }
    @Override
    @Transactional
    public List<SupplyResponse> findAlls() {
        List<Supply> supplies = supplyRepository.findAll();
        return supplies.stream().map(supplyMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SupplyItemResponse> findAllBySupplierPin(String pin) {
        List<SupplyItem> supplies = supplyItemRepository.findAllBySupplierPin(pin);
        return supplies.stream().map(supplyItemMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<SupplyResponse> getAllSuppliers(int page,
                                                int size,
                                                Optional<Boolean> sortOrder,
                                                String sortBy,
                                                Optional<Long> storageId,
                                                Optional<SupplyStatus> status,
                                                Optional<Long> supplierId) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Pageable paging;

        Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
        paging = PageRequest.of(page, size, direction, sortBy);

        Page<Supply> suppliesPage;

        if (storageId.isPresent() && status.isPresent()) {
            suppliesPage = supplyRepository.findByStorageIdAndStatus(storageId.get(), status.get(), paging);
        } else if (storageId.isPresent()) {
            suppliesPage = supplyRepository.findByStorageId(storageId.get(), paging);
        } else if (status.isPresent()) {
            suppliesPage = supplyRepository.findByStatus(status.get(), paging);
        } else if (supplierId.isPresent()) {
            suppliesPage = supplyRepository.findBySupplierId(supplierId.get(), paging);
        } else {
            suppliesPage = supplyRepository.findAll(paging);
        }

        return suppliesPage.map(supplyMapper::entityToResponse);
    }

    @Override
    @Transactional
    public SupplyResponse create(SupplyRequest supplyRequest, String supplierPin, String supplierPassword) {
        log.info("Creating supply with request: {}, supplierPin: {}", supplyRequest, supplierPin);

        // Проверяем валидность логина и пароля поставщика
        if (!supplierService.validateLogin(supplierPin, supplierPassword)) {
            throw new IllegalArgumentException("Неверные логин или пароль");
        }

        // Получаем поставщика по пину
        Supplier supplier = supplierRepository.findByPin(supplierPin);
        log.info("Found supplier: {}", supplier);

        // Создаем объект поставки
        Supply supply = supplyMapper.requestToEntity(supplyRequest);

        // Устанавливаем склад
        Storage storage = storageRepository.findById(supplyRequest.getStorageId())
                .orElseThrow(() -> new IllegalArgumentException("Склад с указанным ID не найден"));
        supply.setStorage(storage);
        log.info("Found storage: {}", storage);

        // Если есть список товаров в запросе, обрабатываем их
        if (supplyRequest.getSupplyItems() != null) {
            List<SupplyItem> supplyItems = new ArrayList<>();

            for (SupplyItemRequest itemRequest : supplyRequest.getSupplyItems()) {
                SupplyItem supplyItem = supplyItemMapper.requestToEntity(itemRequest);
                supplyItem.setProduct(productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Товар с указанным ID не найден")));
                supplyItem.setSupply(supply);
                supplyItems.add(supplyItem);

                log.info("Adding item to supply: productId={}, quantity={}", itemRequest.getProductId(), itemRequest.getQuantity());

                // Обновляем количество товара на складе
                storageService.updateStock(itemRequest.getProductId(), supplyRequest.getStorageId(), itemRequest.getQuantity());
            }

            supply.setSupplyItems(supplyItems);
        } else {
            // Если список товаров не указан, попробуем получить товары из WishList
            WishList wishList = wishListRepository.findByStorageIdAndIsServedFalse(supplyRequest.getStorageId());

            if (wishList != null && !wishList.getWishListItems().isEmpty()) {
                List<SupplyItem> supplyItems = new ArrayList<>();

                for (WishListItem wishListItem : wishList.getWishListItems()) {
                    SupplyItem supplyItem = new SupplyItem();
                    supplyItem.setProduct(wishListItem.getProduct());
                    supplyItem.setSupply(supply);
                    supplyItem.setQuantity(wishListItem.getQuantity());
                    supplyItems.add(supplyItem);

                    log.info("Adding item to supply from wishlist: productId={}, quantity={}", wishListItem.getProduct().getId(), wishListItem.getQuantity());

                    // Обновляем количество товара на складе
                    storageService.updateStock(wishListItem.getProduct().getId(), supplyRequest.getStorageId(), wishListItem.getQuantity());
                }

                supply.setSupplyItems(supplyItems);
            }
        }

        // Устанавливаем поставщика для поставки
        supply.setSupplier(supplier);

        // Сохраняем поставку в базу данных
        Supply savedSupply = supplyRepository.save(supply);
        log.info("Saved supply: {}", savedSupply);

        return supplyMapper.entityToResponse(savedSupply);
    }

    @Override
    @Transactional
    public SupplyResponse createFromWishList(long wishListId, String supplierUsername, String supplierPassword) {
        log.info("Creating supply from wishlist with id: {}, supplierUsername: {}", wishListId, supplierUsername);

        // Проверяем валидность логина и пароля поставщика
        if (!supplierService.validateLogin(supplierUsername, supplierPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Получаем информацию о WishList
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new IllegalArgumentException("WishList with ID " + wishListId + " not found"));
        log.info("Found wishlist: {}", wishList);

        wishList.setIsServed(true);

        // Создаем объект поставки
        Supply supply = new Supply();
        supply.setStatus(SupplyStatus.SENT);  // Устанавливаем статус отправлено
        supply.setSupplier(supplierRepository.findByPin(supplierUsername)); // Устанавливаем поставщика
        log.info("Found supplier: {}", supply.getSupplier());

        // Получаем склад для поставки
        Storage storage = storageRepository.findById(wishList.getStorage().getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage with ID " + wishList.getStorage().getId() + " not found"));
        supply.setStorage(storage);
        log.info("Found storage: {}", storage);

        // Создаем список товаров для поставки на основе товаров из WishList
        List<SupplyItem> supplyItems = new ArrayList<>();
        for (WishListItem wishListItem : wishList.getWishListItems()) {
            SupplyItem supplyItem = new SupplyItem();
            supplyItem.setProduct(wishListItem.getProduct());
            supplyItem.setQuantity(wishListItem.getQuantity());
            supplyItem.setSupply(supply);
            supplyItems.add(supplyItem);

            log.info("Adding item to supply from wishlist: productId={}, quantity={}", wishListItem.getProduct().getId(), wishListItem.getQuantity());
        }

        // Устанавливаем список товаров в поставку
        supply.setSupplyItems(supplyItems);

        // Сохраняем поставку в базу данных
        Supply savedSupply = supplyRepository.save(supply);
        log.info("Saved supply: {}", savedSupply);

        return supplyMapper.entityToResponse(savedSupply);
    }

    @Override
    @Transactional
    public void confirmDelivery(Long supplyId) {
        Supply supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new RecordNotFoundException("Поставщик не найден с id: " + supplyId));

        if (supply.getStatus() != SupplyStatus.SENT) {
            throw new IllegalStateException("Только поставка со статусом  'SENT' Может быть подтверждена");
        }

        supply.setStatus(SupplyStatus.DELIVERED);
        supplyRepository.save(supply);

        // Обновляем количество товаров на складе
        for (SupplyItem supplyItem : supply.getSupplyItems()) {
            storageService.updateStock(supplyItem.getProduct().getId(), supply.getStorage().getId(), supplyItem.getQuantity());
        }
    }

//    @Override
//    @Transactional
//    public void rejectSupply(Long supplyId) {
//        Supply supply = supplyRepository.findById(supplyId)
//                .orElseThrow(() -> new RecordNotFoundException("Supply not found with id: " + supplyId));
//
//        if (supply.getStatus() != SupplyStatus.SENT) {
//            throw new IllegalStateException("Only supplies with status 'SENT' can be rejected");
//        }
//
//        // Обновляем статус поставки
//        supply.setStatus(SupplyStatus.REJECTED);
//        supplyRepository.save(supply);
//
//        // Уменьшаем количество товаров на складе и удаляем их из поставки
//        for (SupplyItem supplyItem : supply.getSupplyItems()) {
//            StorageItem storageItem = storageItemRepository.findByProductIdAndStorageId(
//                            supplyItem.getProduct().getId(), supply.getStorage().getId())
//                    .orElseThrow(() -> new RecordNotFoundException("Product not found in storage"));
//
//            int newQuantity = storageItem.getQuantity() - supplyItem.getQuantity();
//            if (newQuantity < 0) {
//                throw new IllegalStateException("Quantity on storage is less than the supply quantity");
//            }
//
//            storageItem.setQuantity(newQuantity);
//            storageItemRepository.save(storageItem);
//        }
//    }

    @Override
    @Transactional
    public void rejectSupply(Long supplyId) {
        Supply supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new RecordNotFoundException("Поставщик не найден с  id: " + supplyId));

        if (supply.getStatus() != SupplyStatus.SENT) {
            throw new IllegalStateException("Только поставка со статусом 'SENT' может быть отказана");
        }

        // Обновляем статус поставки
        supply.setStatus(SupplyStatus.REJECTED);
        supplyRepository.save(supply);

        // Не изменяем количество товаров на складе, так как поставка была отклонена
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







    @Override
    @Transactional
    public List<WishListResponse> getAllWishListItems() {
        List<WishList> wishListItems = wishListRepository.findAll();
        return wishListItems.stream().map(wishListMapper::entityToResponse).collect(Collectors.toList());
    }




    @Override
    @Transactional
    public Page<WishListResponse> getAllWishListItemsPaged(int page,
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
        Page<WishList> wishListItemsPage = wishListRepository.findAll(paging);

        return wishListItemsPage.map(wishListMapper::entityToResponse);
    }










}
