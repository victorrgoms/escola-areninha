package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.TipoUsuario;

public record DadosCadastroUsuarioDTO(
        String nome,
        String email,
        String senha,
        TipoUsuario tipoUsuario,
        String turnoLotado,
        Long areninhaId
) {}