package com.example.appliances.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String surname;
    String patronymic;

    Integer age;
    String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "discount_category_id")
    DiscountCategory discountCategory;

    @ManyToOne
    @JoinColumn
    ClientType clientType;

    @ManyToOne
    @JoinColumn
    Gender gender;
}
