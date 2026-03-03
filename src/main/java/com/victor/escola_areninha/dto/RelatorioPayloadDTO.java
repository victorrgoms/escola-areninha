package com.victor.escola_areninha.dto;
import java.util.List;
import java.util.Map;

public record RelatorioPayloadDTO(
        List<DadosFrequenciaBatchDTO> frequencias,
        Map<String, Integer> totais,
        String assinaturaBase64
) {}