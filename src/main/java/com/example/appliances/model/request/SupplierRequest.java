package com.example.appliances.model.request;

import com.example.appliances.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierRequest {
    String name;
    String surname;
    String patronymic;
    String phoneNumber;
    String email;

    String password;
    String pin;
//    Boolean isActive;

   Long roleId;

}
