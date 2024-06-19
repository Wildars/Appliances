package com.example.appliances.model.request;

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
public class UserRequest {

    String pin;
    String surname;
    String name;
    String patronymic;
    List<Long> filialIds;
    String phone;
    @Email(message = "Провертье правильность Email")
    String email;
    @NotEmpty(message = "Пароль не должен быть пустым")
    String password;
    List<Long> roleIds;
    Boolean isActive;

}
