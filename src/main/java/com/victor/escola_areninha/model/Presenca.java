package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "presenca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // liga o aluno na lista do dia
    @ManyToOne
    @JoinColumn(name = "frequencia_id")
    private Frequencia frequencia;

    @Column(name = "nome_participante")
    private String nomeParticipante;

    // presente ou ausente
    private String status;
}
