package com.victor.escola_areninha.config;

import com.victor.escola_areninha.repository.UsuarioRepository;
import com.victor.escola_areninha.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // se chegou ate aqui, o cara mandou o token. agora extrair o email
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByEmail(subject).orElseThrow();

            // cria o objeto de autenticacao que o spring entende
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            // forca a autenticacao no contexto dessa requisicao
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // segue o fluxo normal da aplicacao
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            // tira a palavra Bearer e deixa so o hash do token
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}