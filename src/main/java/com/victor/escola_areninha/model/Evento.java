package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    // text pra aguentar textos maiores na descricao
    @Column(columnDefinition = "TEXT")
    private String descricao;

    private LocalDate data;

    // atividade, feriado ou evento
    private TipoEvento tipoEvento;

    // fk ligando o evento na areninha certa
    @ManyToOne
    @JoinColumn(name = "areninha_id")
    private Areninha areninha;
}