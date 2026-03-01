package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.TipoUsuario;
import com.victor.escola_areninha.model.Usuario;

public record DadosListagemEquipeDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        String turnoLotado
) {
    // construtor p facilitar na hora de converter a lista
    public DadosListagemEquipeDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getTipoUsuario(), usuario.getTurnoLotado());
    }
}