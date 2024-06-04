package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListFilialResponse {
    Long id;
    FilialResponse filial;
    Boolean isServed;
    List<WishListItemFilialResponse> wishListItemFilials;

}
