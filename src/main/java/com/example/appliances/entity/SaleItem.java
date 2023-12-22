package com.example.appliances.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItem extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    Product product;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn
//    Sale sale;


    Integer numberNakladnoy;
    String address;

    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    Double totalPrice;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn
//    User user;


}
