package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Areninha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreninhaRepository extends JpaRepository<Areninha, Long> {
    // o crud basico (salvar, listar, deletar) ja ta pronto aqui
}