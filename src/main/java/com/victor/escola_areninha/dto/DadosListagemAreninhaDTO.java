package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.Areninha;

public record DadosListagemAreninhaDTO(Long id, String nome, String endereco) {

    // construtor pra converter a entidade direto pro dto
    public DadosListagemAreninhaDTO(Areninha areninha) {
        this(areninha.getId(), areninha.getNome(), areninha.getEndereco());
    }
}