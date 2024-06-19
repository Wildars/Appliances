package com.example.appliances.model.response;

import com.example.appliances.entity.Storage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferResponse {
    private StorageResponse fromStorage;
    private FilialResponse toFilial;
//    private int quantity;
    LocalDateTime transferDate;

}
