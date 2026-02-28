package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosLoginDTO;
import com.victor.escola_areninha.dto.TokenJWTDTO;
import com.victor.escola_areninha.model.Usuario;
import com.victor.escola_areninha.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody DadosLoginDTO dados) {
        // monta o token cru com o que veio da requisicao
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // joga pro spring security bater la no banco e ver se a senha confere
        var authentication = manager.authenticate(authenticationToken);

        // se passou da linha de cima, o login ta certo. agora é só gerar o jwt
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // devolve o token envelopado no nosso dto
        return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
    }
}