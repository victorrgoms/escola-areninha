package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {

    List<Frequencia> findByResponsavelIdAndDataBetweenOrderByDataDesc(Long responsavelId, LocalDate inicio, LocalDate fim);
}