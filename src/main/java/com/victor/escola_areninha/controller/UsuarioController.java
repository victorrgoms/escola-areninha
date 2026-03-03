package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroUsuarioDTO;
import com.victor.escola_areninha.dto.DadosListagemEquipeDTO;
import com.victor.escola_areninha.model.Usuario;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private AreninhaRepository areninhaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroUsuarioDTO dados) {
        if (repository.findByEmail(dados.email()).isPresent()) {
            return ResponseEntity.badRequest().body("email ja ta em uso mano");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));
        novoUsuario.setTipoUsuario(dados.tipoUsuario());
        novoUsuario.setTurnoLotado(dados.turnoLotado());

        // Busca a areninha no banco e vincula ao usuário
        if (dados.areninhaId() != null) {
            var areninha = areninhaRepository.findById(dados.areninhaId())
                    .orElseThrow(() -> new RuntimeException("Areninha não encontrada"));
            novoUsuario.setAreninha(areninha);
        }

        repository.save(novoUsuario);

        return ResponseEntity.ok("Usuario criado!");
    }

    @GetMapping("/equipe/{areninhaId}")
    // qualquer pessoa com a app instalada pode ver isto, nao leva preauthorize
    public ResponseEntity<List<DadosListagemEquipeDTO>> listarEquipeDaAreninha(@PathVariable Long areninhaId) {

        // vai ao banco buscar o pessoal e converte logo pro dto
        var equipa = repository.findByAreninhaId(areninhaId).stream()
                .map(DadosListagemEquipeDTO::new)
                .toList();

        return ResponseEntity.ok(equipa);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> dadosDoUsuarioLogado(Principal principal) {
        return ResponseEntity.ok(repository.findByEmail(principal.getName()).get());
    }
}