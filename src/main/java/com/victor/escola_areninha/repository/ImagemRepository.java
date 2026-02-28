package com.victor.escola_areninha.repository;

import com.victor.escola_areninha.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

    // carregar a galeria filtrando pela areninha
    List<Imagem> findByAreninhaId(Long areninhaId);
}