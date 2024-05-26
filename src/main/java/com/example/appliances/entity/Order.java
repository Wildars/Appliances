package com.example.appliances.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends Audit<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<OrderItem> orderItems = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "status")
    SaleStatus status;

    String address;
    String phoneNumber;

    Date dateDelivery;

    String name;
    String comment;

    @Column(name = "total_amount")
    Double totalAmount;
}
