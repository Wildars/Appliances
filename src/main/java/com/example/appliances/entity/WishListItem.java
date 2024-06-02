package com.example.appliances.entity;

import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    WishList wishList;

    Integer quantity;


}
