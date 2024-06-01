package com.example.appliances.model.response;

import com.example.appliances.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferItemResponse {

    TransferResponse transfer;

    int quantity;

    Long id;

    ProductResponse product;

}
