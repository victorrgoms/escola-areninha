package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areninha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Areninha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;

    private Double latitude;
    private Double longitude;
}