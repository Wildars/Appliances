package com.example.appliances.model.request;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Role;
import com.example.appliances.model.response.FilialResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationModel {
    Long id;
    String pin;
    String surname;
    String name;
    String patronymic;
    FilialResponse organizations;
    String phone;
    String email;
    List<Role> roles;
    List<String> permissions;
    String jwtToken;
}
