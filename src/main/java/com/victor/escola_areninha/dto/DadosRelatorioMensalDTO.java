package com.victor.escola_areninha.dto;

public record DadosRelatorioMensalDTO(
        int mes,
        int ano,
        String turmas,
        String turno,
        String assinaturaUrl,
        String areaConhecimento,
        String nomeCoordenador,
        int aulas6ano,
        int aulas7ano,
        int aulas8ano,
        int aulas9ano
) {}