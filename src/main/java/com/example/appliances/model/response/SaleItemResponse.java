package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItemResponse {
    Long id;

    String name;

    ProductResponse product;


    SaleResponse sale;

    Integer quantity;

    Double price;


    UserResponse user;


}
