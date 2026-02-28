package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.Evento;
import com.victor.escola_areninha.model.TipoEvento;
import java.time.LocalDate;

public record DadosListagemEventoDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDate data,
        TipoEvento tipoEvento
) {
    public DadosListagemEventoDTO(Evento evento) {
        this(evento.getId(), evento.getTitulo(), evento.getDescricao(), evento.getData(), evento.getTipoEvento());
    }
}