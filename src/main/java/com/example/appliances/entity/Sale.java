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
public class Sale extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    LocalDateTime dateProdajy;
    String comments;
    Integer numberNakladnoy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    Filial filial;
}
