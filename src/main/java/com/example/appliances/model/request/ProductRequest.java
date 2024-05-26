package com.example.appliances.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String description;
    String code;

    BigDecimal price;
    List<Long> categoryIds;
    List<ProductFieldRequest> fields;
    String  photoPath;


    Long brandId;
    Long producingCountryId;
}
