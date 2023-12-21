package com.example.appliances.model.request;

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
public class SaleRequest {
    String name;

    LocalDateTime dateProdajy;
    String comments;
    Integer numberNakladnoy;


    Long userId;
    String address;


    Long filialId;
}
