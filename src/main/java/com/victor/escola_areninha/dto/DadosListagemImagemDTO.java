package com.victor.escola_areninha.dto;

import com.victor.escola_areninha.model.Imagem;
import java.time.LocalDateTime;

public record DadosListagemImagemDTO(
        Long id,
        String url,
        String descricao,
        String nomeUsuario,
        LocalDateTime dataUpload
) {
    public DadosListagemImagemDTO(Imagem imagem) {
        this(imagem.getId(), imagem.getUrl(), imagem.getDescricao(),
                imagem.getUsuario().getNome(), imagem.getDataUpload());
    }
}