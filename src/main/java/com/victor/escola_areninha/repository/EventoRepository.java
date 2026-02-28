package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // pra quando o app pedir o calendario de uma areninha especifica
    List<Evento> findByAreninhaId(Long areninhaId);
}
