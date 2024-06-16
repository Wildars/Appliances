package com.example.appliances.statistics;

import com.example.appliances.repository.StorageItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Override
    @Transactional
    public InventoryStatisticsResponse getInventoryStatistics(Long storageId) {
        Long totalInventory = storageItemRepository.findTotalInventoryByStorageId(storageId);
        List<Object[]> results = storageItemRepository.findInventoryByCategoryAndStorageId(storageId);

        List<CategoryInventory> categoryInventories = results.stream()
                .map(result -> new CategoryInventory((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());

        return InventoryStatisticsResponse.builder()
                .totalInventory(totalInventory)
                .categoryInventories(categoryInventories)
                .build();
    }
}
