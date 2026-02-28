package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // a gente vai usar muito isso aqui na hora de validar o login do cara
    Optional<Usuario> findByEmail(String email);
}