package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.HorarioAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAulaRepository extends JpaRepository<HorarioAula, Long> {

    // montar a grade de horarios de uma unidade
    List<HorarioAula> findByAreninhaId(Long areninhaId);
}