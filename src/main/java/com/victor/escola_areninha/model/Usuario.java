package com.victor.escola_areninha.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Column(name = "turno_lotado")
    private String turnoLotado;

    @Column(name = "area_conhecimento")
    private String areaConhecimento;

    @ManyToOne
    @JoinColumn(name = "areninha_id")
    private Areninha areninha;

    // Se apagar apaga também as fotos dele"
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Imagem> fotos;

    // Se apagar este apaga os registos de frequência que ele fez
    @JsonIgnore
    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Frequencia> frequencias;

    // -------------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}