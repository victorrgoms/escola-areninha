package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroEventoDTO;
import com.victor.escola_areninha.dto.DadosListagemEventoDTO;
import com.victor.escola_areninha.model.Evento;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private AreninhaRepository areninhaRepository;

    @PostMapping
    // restringe a criacao de eventos no calendario
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroEventoDTO dados) {

        // valida se a areninha existe antes de tentar vincular
        var areninha = areninhaRepository.findById(dados.areninhaId())
                .orElseThrow(() -> new RuntimeException("Areninha não encontrada com esse ID"));

        var evento = new Evento();
        evento.setTitulo(dados.titulo());
        evento.setDescricao(dados.descricao());
        evento.setData(dados.data());
        evento.setTipoEvento(dados.tipoEvento());
        evento.setAreninha(areninha);

        repository.save(evento);

        return ResponseEntity.ok("Evento inserido na agenda");
    }

    @GetMapping("/areninha/{areninhaId}")
    public ResponseEntity<List<DadosListagemEventoDTO>> listarPorAreninha(@PathVariable Long areninhaId) {

        // puxa os eventos filtrando pela unidade e ja converte pra dto
        var lista = repository.findByAreninhaId(areninhaId).stream()
                .map(DadosListagemEventoDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }
}