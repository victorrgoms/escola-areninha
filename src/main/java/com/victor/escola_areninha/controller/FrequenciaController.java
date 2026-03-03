package com.victor.escola_areninha.controller;

import com.victor.escola_areninha.dto.DadosCadastroFrequenciaDTO;
import com.victor.escola_areninha.dto.DadosFrequenciaBatchDTO;
import com.victor.escola_areninha.dto.DadosRelatorioMensalDTO;
import com.victor.escola_areninha.dto.RelatorioPayloadDTO;
import com.victor.escola_areninha.model.Frequencia;
import com.victor.escola_areninha.repository.AreninhaRepository;
import com.victor.escola_areninha.repository.FrequenciaRepository;
import com.victor.escola_areninha.repository.UsuarioRepository;
import com.victor.escola_areninha.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<String> registrar(@RequestBody DadosCadastroFrequenciaDTO dados, Principal principal) {
        var responsavel = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario logado nao encontrado no banco"));

        var areninha = areninhaRepository.findById(dados.areninhaId())
                .orElseThrow(() -> new RuntimeException("Areninha informada nao existe"));

        var frequencia = new Frequencia();
        frequencia.setData(dados.data());
        frequencia.setAtividade(dados.atividade());
        frequencia.setHorario(dados.horario());
        frequencia.setAreninha(areninha);
        frequencia.setResponsavel(responsavel);
        frequencia.setPdfUrl(null);

        repository.save(frequencia);
        return ResponseEntity.ok("Frequencia salva com sucesso");
    }

    // NOVO ENDPOINT: Recebe a lista do App, salva no banco e gera o PDF de uma vez
    @PostMapping("/relatorio")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<byte[]> gerarRelatorioDaLista(@RequestBody RelatorioPayloadDTO payload, Principal principal) {

        var monitor = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Frequencia> frequenciasSalvas = new ArrayList<>();

        for (DadosFrequenciaBatchDTO dto : payload.frequencias()) {
            var areninha = areninhaRepository.findById(dto.areninhaId())
                    .orElseThrow(() -> new RuntimeException("Areninha não encontrada"));

            var freq = new Frequencia();
            // Converte a string "DD/MM/AAAA" que vem do celular para o LocalDate do Java
            freq.setData(LocalDate.parse(dto.data(), formatter));
            freq.setAtividade(dto.atividade());
            freq.setResponsavel(monitor);
            freq.setAreninha(areninha);

            // Salva cada frequência no banco para manter o histórico
            frequenciasSalvas.add(repository.save(freq));
        }

        int mes = frequenciasSalvas.get(0).getData().getMonthValue();
        int ano = frequenciasSalvas.get(0).getData().getYear();

        // passa o mes e ano reais, e preenche o resto com vazio/zero so pro java deixar compilar
        // o pdf service se vira pra pegar o resto dos dados das entidades
        var dadosRelatorio = new DadosRelatorioMensalDTO(
                mes,
                ano,
                "",
                "",
                "",
                "",
                "",
                0,
                0,
                0,
                0
        );

        byte[] arquivoPdf = pdfService.gerarRelatorioMensal(frequenciasSalvas, monitor, dadosRelatorio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_areninha.pdf\"")
                .body(arquivoPdf);
    }
}