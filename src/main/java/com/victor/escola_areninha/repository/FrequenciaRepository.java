package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {

    // historico de chamadas de uma areninha
    List<Frequencia> findByAreninhaId(Long areninhaId);
}