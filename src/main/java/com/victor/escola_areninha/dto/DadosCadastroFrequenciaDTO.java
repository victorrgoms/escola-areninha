package com.victor.escola_areninha.dto;

import java.time.LocalDate;

public record DadosCadastroFrequenciaDTO(
        LocalDate data,
        String atividade,
        String horario,
        Long areninhaId
) {}