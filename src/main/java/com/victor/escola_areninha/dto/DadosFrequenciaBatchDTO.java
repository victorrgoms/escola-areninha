package com.victor.escola_areninha.dto;

public record DadosFrequenciaBatchDTO(
        String id,
        String data,
        String turma,
        String atividade,
        Long areninhaId,
        String turno,
        String horario
) {}