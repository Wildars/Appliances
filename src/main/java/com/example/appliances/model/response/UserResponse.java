package com.example.appliances.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String pin;
    String surname;
    String name;
    String patronymic;
    List<FilialResponse> filials;
    String phone;
    @Email(message = "Провертье правильность Email")
    String email;
    @NotEmpty(message = "Пароль не должен быть пустым")
    String password;
    List<RoleResponse> roles;
    Boolean isActive;

}