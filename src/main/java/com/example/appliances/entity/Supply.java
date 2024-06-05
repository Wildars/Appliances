package com.example.appliances.entity;

import com.example.appliances.enums.SupplyStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class Supply extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<SupplyItem> supplyItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    Storage storage;
//    private SupplyStatus status;


    @ManyToOne // Добавлено поле для хранения информации о поставщике
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @Enumerated(EnumType.STRING)
    private SupplyStatus status;
}
