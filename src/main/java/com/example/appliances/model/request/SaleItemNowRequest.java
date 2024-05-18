package com.example.appliances.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItemNowRequest {
    String name;

//    Long productId;

    List<Long> productIds;

//    Long saleId;
//    LocalDateTime schedule;
    Integer quantity;
    Long clientId;

    String phoneNumber;
    Double totalPrice;

    String address;
//    Long userId;




}
