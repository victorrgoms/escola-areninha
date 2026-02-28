package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroAreninhaDTO;
import com.victor.escola_areninha.dto.DadosListagemAreninhaDTO;
import com.victor.escola_areninha.model.Areninha;
import com.victor.escola_areninha.repository.AreninhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areninhas")
public class AreninhaController {

    @Autowired
    private AreninhaRepository repository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // so a prefeitura/admin pode criar unidade nova
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroAreninhaDTO dados) {
        var areninha = new Areninha();
        areninha.setNome(dados.nome());
        areninha.setEndereco(dados.endereco());
        areninha.setLatitude(dados.latitude());
        areninha.setLongitude(dados.longitude());

        repository.save(areninha);

        return ResponseEntity.ok("Areninha cadastrada no sistema");
    }

    @GetMapping
    // qualquer um logado pode ver a lista, entao nao precisa de preauthorize aqui
    public ResponseEntity<List<DadosListagemAreninhaDTO>> listar() {
        // puxa do banco e ja mapeia pro dto pra nao vazar dado a mais
        var lista = repository.findAll().stream().map(DadosListagemAreninhaDTO::new).toList();
        return ResponseEntity.ok(lista);
    }
}