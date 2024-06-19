package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    UUID id;
    String name;
    Double price;
    String code;
    String description;
    List<ProductCategoryResponse> categories;
    List<ProductFieldResponse> fields;
//    Status status;
    BrandResponse brand;
    ProducingCountryResponse producingCountry;
    List<String> photoPaths;
}
