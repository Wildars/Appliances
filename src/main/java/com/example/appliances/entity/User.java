package com.example.appliances.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "use")
public class User extends Audit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String pin;
    @Column(nullable = false)
    String surname;
    @Column(nullable = false)
    String name;
    @Column
    String patronymic;


    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "use_filials",
            joinColumns = @JoinColumn(name = "use_id"),
            inverseJoinColumns = @JoinColumn(name = "filial_id"))
    List<Filial> filials;
    @Column(nullable = false)
    String phone;
    String email;
    @Column(nullable = false)
    String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    List<Role> roles;
    @Column(columnDefinition = "boolean default true")
    Boolean isActive;

    String address;




    public String getFIO() {
        if (this.patronymic == null)
            return this.surname + " " + this.name;
        else
            return this.surname + " " + this.name + " " + this.patronymic;
    }

}
