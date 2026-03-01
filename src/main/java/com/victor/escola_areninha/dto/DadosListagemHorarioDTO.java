package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.HorarioAula;
import java.time.LocalTime;

public record DadosListagemHorarioDTO(
        Long id,
        String diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        String atividade,
        String turma
) {
    // construtor pra converter a entidade
    public DadosListagemHorarioDTO(HorarioAula horario) {
        this(horario.getId(), horario.getDiaSemana(), horario.getHoraInicio(),
                horario.getHoraFim(), horario.getAtividade(), horario.getTurma());
    }
}