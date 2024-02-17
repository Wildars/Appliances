package com.example.appliances.model.response;

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
    Integer numberNakladnoy;
    List<ProductResponse> products;
    String address;
    LocalDateTime schedule;

//    SaleResponse sale;

    Integer quantity;

    Double totalPrice;


//    UserResponse user;


}
