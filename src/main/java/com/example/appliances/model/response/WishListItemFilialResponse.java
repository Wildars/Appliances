package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListItemFilialResponse {
    Long id;
    ProductResponse product;
//    WishListResponse wishList;
    int quantity;
}
