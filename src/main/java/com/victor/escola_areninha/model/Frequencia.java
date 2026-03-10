package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "frequencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Frequencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private String atividade;
    private String horario;

    @ManyToOne
    @JoinColumn(name = "areninha_id")
    private Areninha areninha;

    // o monitor ou supervisor q ta preenchendo a chamada
    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    // link do pdf dps de gerado
    @Column(name = "pdf_url")
    private String pdfUrl;

    private String turno;
}