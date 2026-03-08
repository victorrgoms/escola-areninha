package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.Imagem;
import java.time.format.DateTimeFormatter;

public record DadosListagemImagemDTO(
        Long id,
        String url,
        String descricao,
        String nomeUsuario,
        String nomeAreninha, // NOVO CAMPO: Identifica a areninha da foto
        String dataFormatada
) {
    public DadosListagemImagemDTO(Imagem img) {
        this(
                img.getId(),
                img.getUrl(),
                img.getDescricao(),
                img.getUsuario().getNome(),
                img.getAreninha() != null ? img.getAreninha().getNome() : "Areninha não informada",
                img.getDataUpload().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
}