package com.example.appliances.entity;

import com.example.appliances.enums.SupplyStatus;
import com.example.appliances.enums.WishListStatusEnum;
import com.example.appliances.service.WishListFilialService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListFilial extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Boolean isServed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    Filial filial;

    @OneToMany(mappedBy = "wishListFilial", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<WishListItemFilial> wishListItemFilials = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    WishListStatusEnum status;

    String comments;
}
