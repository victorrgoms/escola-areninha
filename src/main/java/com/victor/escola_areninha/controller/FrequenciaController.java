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

    @PostMapping("/relatorio-mensal/gerar")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<byte[]> gerarRelatorioMensal(
            @RequestBody com.victor.escola_areninha.dto.DadosRelatorioMensalDTO dados,
            Principal principal) {

        var email = principal.getName();
        var monitor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var frequenciasDoMes = repository.findAll().stream()
                .filter(f -> f.getResponsavel().getId().equals(monitor.getId()))
                .filter(f -> f.getData().getMonthValue() == dados.mes() && f.getData().getYear() == dados.ano())
                .toList();

        if (frequenciasDoMes.isEmpty()) {
            throw new RuntimeException("Nenhuma frequência encontrada para este mês.");
        }

        // Agora passamos o DTO inteiro pro Service resolver a mágica
        byte[] arquivoPdf = pdfService.gerarRelatorioMensal(frequenciasDoMes, monitor, dados);

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_" + dados.mes() + "_" + dados.ano() + ".pdf\"")
                .body(arquivoPdf);
    }
}