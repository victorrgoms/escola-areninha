package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroImagemDTO;
import com.victor.escola_areninha.dto.DadosListagemImagemDTO;
import com.victor.escola_areninha.model.Imagem;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.ImagemRepository;
import com.victor.escola_areninha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/imagens")
public class ImagemController {

    @Autowired
    private ImagemRepository repository;

    @Autowired
    private AreninhaRepository areninhaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroImagemDTO dados, Principal principal) {

        // pega o usuario logado pelo token
        var usuario = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("usuario nao encontrado no banco"));

        // verifica se a areninha informada existe
        var areninha = areninhaRepository.findById(dados.areninhaId())
                .orElseThrow(() -> new RuntimeException("areninha informada nao existe"));

        var imagem = new Imagem();
        imagem.setUrl(dados.url());
        imagem.setDescricao(dados.descricao());
        imagem.setDataUpload(LocalDateTime.now());
        imagem.setUsuario(usuario);
        imagem.setAreninha(areninha);

        repository.save(imagem);

        return ResponseEntity.ok("imagem salva na galeria");
    }

    @GetMapping("/areninha/{areninhaId}")
    public ResponseEntity<List<DadosListagemImagemDTO>> listarPorAreninha(@PathVariable Long areninhaId) {

        // lista as fotos da unidade e ja mapeia pro dto pra tela
        var lista = repository.findByAreninhaId(areninhaId).stream()
                .map(DadosListagemImagemDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }
}