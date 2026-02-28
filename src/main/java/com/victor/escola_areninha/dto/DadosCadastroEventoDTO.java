package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.TipoEvento;
import java.time.LocalDate;

public record DadosCadastroEventoDTO(
        String titulo,
        String descricao,
        LocalDate data,
        TipoEvento tipoEvento,
        Long areninhaId
) {}