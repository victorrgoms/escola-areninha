package com.victor.escola_areninha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // o pdf pede index pro email, o unique ja ajuda nisso e evita cadastro duplicado
    @Column(unique = true)
    private String email;

    private String senha;

    // monitor, supervisor ou admin
    private TipoUsuario tipoUsuario;

    // manha, tarde ou ambos
    @Column(name = "turno_lotado")
    private String turnoLotado;

    // relaciona o usuario com a areninha dele
    @ManyToOne
    @JoinColumn(name = "areninha_id")
    private Areninha areninha;
}