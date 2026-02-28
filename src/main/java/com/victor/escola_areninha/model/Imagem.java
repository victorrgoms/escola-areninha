package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link salvo na nuvem
    private String url;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    // quem fez o post
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "areninha_id")
    private Areninha areninha;
}
