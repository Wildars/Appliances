package com.example.appliances.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleOrderItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
}