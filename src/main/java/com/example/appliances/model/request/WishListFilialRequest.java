package com.example.appliances.model.request;

import com.example.appliances.entity.WishListItemFilial;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListFilialRequest {


    Long filialId;
     List<WishListItemFilialRequest> wishListItemFilials;
}
