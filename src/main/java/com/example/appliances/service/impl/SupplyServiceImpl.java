package com.example.appliances.service.impl;

import com.example.appliances.entity.*;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SupplyItemMapper;
import com.example.appliances.mapper.SupplyMapper;
import com.example.appliances.mapper.WishListMapper;
import com.example.appliances.model.request.SupplyItemRequest;
import com.example.appliances.model.request.SupplyRequest;
import com.example.appliances.model.response.StorageResponse;
import com.example.appliances.model.response.SupplyItemResponse;
import com.example.appliances.model.response.SupplyResponse;
import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.repository.*;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.SupplierService;
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

    ProductRepository productRepository;
    StorageRepository storageRepository;
    SupplierRepository supplierRepository;
    SupplyItemRepository supplyItemRepository;
    SupplyMapper supplyMapper;

    WishListMapper wishListMapper;

    WishListRepository wishListRepository;
ProductService productService;

    SupplierService supplierService;
    StorageService storageService;

    public SupplyServiceImpl(SupplyRepository supplyRepository, SupplyItemMapper supplyItemMapper, ProductRepository productRepository, StorageRepository storageRepository, SupplierRepository supplierRepository, SupplyItemRepository supplyItemRepository, SupplyMapper supplyMapper, WishListMapper wishListMapper, WishListRepository wishListRepository, ProductService productService, SupplierService supplierService, StorageService storageService) {
        this.supplyRepository = supplyRepository;
        this.supplyItemMapper = supplyItemMapper;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.supplierRepository = supplierRepository;
        this.supplyItemRepository = supplyItemRepository;
        this.supplyMapper = supplyMapper;
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
    public SupplyResponse create(SupplyRequest supplyRequest, String supplierPin, String supplierPassword) {
        // Проверяем валидность логина и пароля поставщика
        if (!supplierService.validateLogin(supplierPin, supplierPassword)) {
            throw new IllegalArgumentException("Неверные логин или пароль");
        }

        // Получаем поставщика по пину
        Supplier supplier = supplierRepository.findByPin(supplierPin);

        // Создаем объект поставки
        Supply supply = supplyMapper.requestToEntity(supplyRequest);

        // Устанавливаем склад
        Storage storage = storageRepository.findById(supplyRequest.getStorageId())
                .orElseThrow(() -> new IllegalArgumentException("Склад с указанным ID не найден"));
        supply.setStorage(storage);

        // Если есть список товаров в запросе, обрабатываем их
        if (supplyRequest.getSupplyItems() != null) {
            List<SupplyItem> supplyItems = new ArrayList<>();

            for (SupplyItemRequest itemRequest : supplyRequest.getSupplyItems()) {
                SupplyItem supplyItem = supplyItemMapper.requestToEntity(itemRequest);
                supplyItem.setProduct(productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Товар с указанным ID не найден")));
                supplyItem.setSupply(supply);
                supplyItems.add(supplyItem);

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

        return supplyMapper.entityToResponse(savedSupply);
    }

    @Override
    @Transactional
    public SupplyResponse createFromWishList(long wishListId, String supplierUsername, String supplierPassword) {
        // Проверяем валидность логина и пароля поставщика
        if (!supplierService.validateLogin(supplierUsername, supplierPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Получаем информацию о WishList
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new IllegalArgumentException("WishList with ID " + wishListId + " not found"));

        // Создаем объект поставки
        Supply supply = new Supply();
        supply.setSupplier(supplierRepository.findByPin(supplierUsername)); // Устанавливаем поставщика

        // Получаем склад для поставки
        Storage storage = storageRepository.findById(wishList.getStorage().getId())
                .orElseThrow(() -> new IllegalArgumentException("Storage with ID " + wishList.getStorage() + " not found"));
        supply.setStorage(storage);

        // Создаем список товаров для поставки на основе товаров из WishList
        List<SupplyItem> supplyItems = new ArrayList<>();
        for (WishListItem wishListItem : wishList.getWishListItems()) {
            SupplyItem supplyItem = new SupplyItem();
            supplyItem.setProduct(wishListItem.getProduct());
            supplyItem.setQuantity(wishListItem.getQuantity());
            supplyItem.setSupply(supply);
            supplyItems.add(supplyItem);
        }

        // Устанавливаем список товаров в поставку
        supply.setSupplyItems(supplyItems);

        // Сохраняем поставку в базу данных
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
