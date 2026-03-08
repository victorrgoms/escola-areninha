package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroImagemDTO;
import com.victor.escola_areninha.dto.DadosListagemImagemDTO;
import com.victor.escola_areninha.model.Imagem;
import com.victor.escola_areninha.repository.ImagemRepository;
import com.victor.escola_areninha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/imagens")
public class ImagemController {

    @Autowired
    private ImagemRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<String> publicarFoto(@RequestBody DadosCadastroImagemDTO dados, Principal principal) {
        var usuarioLogado = usuarioRepository.findByEmail(principal.getName()).get();

        Imagem novaImagem = new Imagem();
        novaImagem.setUrl(dados.base64()); // Guarda o Base64
        novaImagem.setDescricao(dados.descricao());
        novaImagem.setUsuario(usuarioLogado);
        novaImagem.setAreninha(usuarioLogado.getAreninha());

        repository.save(novaImagem);
        return ResponseEntity.ok("Foto publicada com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemImagemDTO>> listarGaleria() {
        // Traz as fotos mais recentes primeiro
        var fotos = repository.findAll(Sort.by(Sort.Direction.DESC, "dataUpload"))
                .stream().map(DadosListagemImagemDTO::new).toList();
        return ResponseEntity.ok(fotos);
    }
}