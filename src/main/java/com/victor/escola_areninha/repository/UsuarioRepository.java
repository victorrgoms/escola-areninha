package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // puxa a equipe de uma areninha especifica
    List<Usuario> findByAreninhaId(Long areninhaId);
}