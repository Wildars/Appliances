package com.example.appliances.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRequest {
    String name;
    String surname;
    String patronymic;
    String address;
    Double procentSkidki;
    Long clientTypeId;
}
