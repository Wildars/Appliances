package com.example.appliances.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleOrderResponse {
    private Long id;
    private List<SimpleOrderItemResponse> orderItems;

    private UserResponse user;
//    private SystemStatus status;
//    private OrderType orderType;
}