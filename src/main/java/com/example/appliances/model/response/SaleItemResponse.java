package com.example.appliances.model.response;

import com.example.appliances.entity.SaleStatus;
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
public class SaleItemResponse {
    Long id;

    String name;

//    ProductResponse product;
    String numberNakladnoy;
    List<ProductResponse> products;
    String address;
    LocalDateTime schedule;

    String phoneNumber;

    SaleStatus saleStatus;
//    SaleResponse sale;

    ClientResponse client;
    Integer quantity;

    Double totalPrice;


//    UserResponse user;


}
