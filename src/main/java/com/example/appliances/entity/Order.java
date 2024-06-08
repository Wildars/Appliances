package com.example.appliances.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Fetch(FetchMode.SUBSELECT)
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

    Boolean isDelivery;

    String description;
    String numberNakladnoy;
    LocalDateTime schedule;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Manager manager;


}
