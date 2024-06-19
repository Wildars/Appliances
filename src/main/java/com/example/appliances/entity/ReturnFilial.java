package com.example.appliances.entity;

import com.example.appliances.enums.ReturnStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnFilial extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_filial_id")
    private Filial fromFilial;

    @ManyToOne
    @JoinColumn(name = "to_storage_id")
    private Storage toStorage;

    @OneToMany(mappedBy = "returnFilial", cascade = CascadeType.ALL)
    private List<ReturnFilialItem> returnFilialItems;

    @Enumerated(EnumType.STRING)
    private ReturnStatusEnum status;

    private LocalDateTime returnDate;
}

