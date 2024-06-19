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
public class ReturnFilialRequest {
    private Long fromFilialId;
    private Long toStorageId;
    private List<ReturnItemFilialRequest> items;
}
