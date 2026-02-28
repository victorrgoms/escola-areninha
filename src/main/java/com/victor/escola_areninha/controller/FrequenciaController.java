package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroFrequenciaDTO;
import com.victor.escola_areninha.model.Frequencia;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.FrequenciaRepository;
import com.victor.escola_areninha.repository.UsuarioRepository;
import com.victor.escola_areninha.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/frequencias")
public class FrequenciaController {

    @Autowired
    private FrequenciaRepository repository;

    @Autowired
    private AreninhaRepository areninhaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PdfService pdfService;

    @PostMapping
    // admin tbm pode preencher caso precise corrigir
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<String> registrar(@RequestBody DadosCadastroFrequenciaDTO dados, Principal principal) {

        // puxa o email direto do token jwt que veio no header
        var emailUsuarioLogado = principal.getName();
        var responsavel = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuario logado nao encontrado no banco"));

        var areninha = areninhaRepository.findById(dados.areninhaId())
                .orElseThrow(() -> new RuntimeException("Areninha informada nao existe"));

        var frequencia = new Frequencia();
        frequencia.setData(dados.data());
        frequencia.setAtividade(dados.atividade());
        frequencia.setHorario(dados.horario());
        frequencia.setAreninha(areninha);
        frequencia.setResponsavel(responsavel);

        // a url do pdf fica nula por enqto
        frequencia.setPdfUrl(null);

        repository.save(frequencia);

        return ResponseEntity.ok("Frequencia salva com sucesso");
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<byte[]> baixarPdf(@PathVariable Long id) {

        var frequencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frequencia não encontrada"));

        byte[] arquivoPdf = pdfService.gerarPdfFrequencia(frequencia);

        // mexe nos headers pra avisar o navegador q isso eh um arquivo pra baixar
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"frequencia_" + id + ".pdf\"")
                .body(arquivoPdf);
    }
}