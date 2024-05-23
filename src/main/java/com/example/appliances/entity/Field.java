package com.example.appliances.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String name;


    String length;

    Boolean fireResistant;

    String width;

    String thickness;

    String manufacturerArticle;

    Integer code;

    String vidKromki;

    String objectPrimeneniya;

    String typePrimeneniya;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    ProductCategory category;
}
