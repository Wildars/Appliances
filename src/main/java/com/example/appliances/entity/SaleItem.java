package com.example.appliances.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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


    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "saleItem_product",
            joinColumns = @JoinColumn(name = "saleItem_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    List<Product> products;

    LocalDateTime schedule;
    String comments;

    String numberNakladnoy;
    String address;
    @Column(name = "phone_number")
    String phoneNumber;
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "sale_status")
    SaleStatus saleStatus;

    Double totalPrice;



}
