package com.example.appliances.model.response;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleResponse {
    Long id;
    String name;

    String address;
    LocalDateTime dateProdajy;
    String comments;
    Integer numberNakladnoy;

    UserResponse user;

    FilialResponse filial;
}
