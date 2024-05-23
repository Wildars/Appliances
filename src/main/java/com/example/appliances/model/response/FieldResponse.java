package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldResponse {
    Long id;
    String name;
    String length;

    Boolean fireResistant;

    String width;

    String thickness;

    String manufacturerArticle;

    Integer code;

    String vidKromki;

    String objectPrimeneniya;

    String typePrimeneniya;


    ProductCategoryResponse productCategory;
}