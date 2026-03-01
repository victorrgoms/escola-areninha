package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroHorarioDTO;
import com.victor.escola_areninha.dto.DadosListagemHorarioDTO;
import com.victor.escola_areninha.model.HorarioAula;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.HorarioAulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioAulaController {

    @Autowired
    private HorarioAulaRepository repository;

    @Autowired
    private AreninhaRepository areninhaRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroHorarioDTO dados) {

        // verifica se a areninha realmente existe
        var areninha = areninhaRepository.findById(dados.areninhaId())
                .orElseThrow(() -> new RuntimeException("Areninha não encontrada, bicho"));

        var horario = new HorarioAula();
        horario.setDiaSemana(dados.diaSemana());
        horario.setHoraInicio(dados.horaInicio());
        horario.setHoraFim(dados.horaFim());
        horario.setAtividade(dados.atividade());
        horario.setTurma(dados.turma());
        horario.setAreninha(areninha);

        repository.save(horario);

        return ResponseEntity.ok("Horário adicionado na grade");
    }

    @GetMapping("/areninha/{areninhaId}")
    public ResponseEntity<List<DadosListagemHorarioDTO>> listarPorAreninha(@PathVariable Long areninhaId) {

        // puxa a lista e ja devolve limpo pro front renderizar na tela
        var lista = repository.findByAreninhaId(areninhaId).stream()
                .map(DadosListagemHorarioDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }
}