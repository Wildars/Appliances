package com.example.appliances.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transfer extends Audit<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime transferDate;

    @ManyToOne
    @JoinColumn(name = "from_storage_id")
    private Storage fromStorage;

    @ManyToOne
    @JoinColumn(name = "to_filial_id")
    private Filial toFilial;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<TransferItem> transferItems = new ArrayList<>();
}