package com.example.appliances.entity;

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
public class Filial extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    String filCode;

//    String address;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<FilialItem> filialItems = new ArrayList<>();


}
