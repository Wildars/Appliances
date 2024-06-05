package com.example.appliances.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListRequest {

    Long storageId;
     List<WishListItemRequest> wishListItems;
}
