package com.victor.escola_areninha.dto;

import java.time.LocalTime;

public record DadosCadastroHorarioDTO(
        String diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        String atividade,
        String turma,
        Long areninhaId
) {}