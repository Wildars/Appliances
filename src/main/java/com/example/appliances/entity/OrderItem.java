package com.example.appliances.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
     Order order;


    //TODO изменить продукт на filialItem
    @ManyToOne
    @JoinColumn(name = "filialItem_id")
     FilialItem filialItem;

     Integer quantity;
}
