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
public class StorageResponse {
    Long id;
    private String name;
    private String storageCode;
    private String address;
    List<StorageItemResponse> storageItems;
}
