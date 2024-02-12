package com.example.appliances.model.response;

import com.example.appliances.entity.Image;
import com.example.appliances.entity.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    Double price;

    String description;
    Status status;
    Image image;
    ProductResponse productCategory;
}
