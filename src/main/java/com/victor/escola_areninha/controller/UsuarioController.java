package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroUsuarioDTO;
import com.victor.escola_areninha.dto.DadosListagemEquipeDTO;
import com.victor.escola_areninha.model.Usuario;
import com.victor.escola_areninha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody DadosCadastroUsuarioDTO dados) {
        // confere se ja tem alguem com esse email pra nao dar erro de constraint
        if (repository.findByEmail(dados.email()).isPresent()) {
            return ResponseEntity.badRequest().body("email ja ta em uso mano");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());

        // joga a senha no bcrypt antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));

        novoUsuario.setTipoUsuario(dados.tipoUsuario());

        repository.save(novoUsuario);

        return ResponseEntity.ok("usuario criado!");
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
}