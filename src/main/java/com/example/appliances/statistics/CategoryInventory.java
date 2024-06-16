package com.example.appliances.statistics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryInventory {
    private String categoryName;
    private Long quantity;
}