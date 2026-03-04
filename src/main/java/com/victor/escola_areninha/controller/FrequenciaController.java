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

    @PostMapping("/relatorio")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<byte[]> gerarRelatorioDaLista(@RequestBody RelatorioPayloadDTO payload, Principal principal) {

        var monitor = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Frequencia> frequenciasSalvas = new ArrayList<>();

        for (DadosFrequenciaBatchDTO dto : payload.frequencias()) {
            var freq = new Frequencia();
            freq.setData(LocalDate.parse(dto.data(), formatter));
            freq.setAtividade(dto.atividade());

            // desce pro banco com a hora exata que o monitor preencheu na tela
            freq.setHorario(dto.horario());

            freq.setResponsavel(monitor);
            freq.setAreninha(monitor.getAreninha());

            frequenciasSalvas.add(repository.save(freq));
        }

        int mes = frequenciasSalvas.get(0).getData().getMonthValue();
        int ano = frequenciasSalvas.get(0).getData().getYear();

        // puxa o turno da primeira aula do lote pra garantir que o cabecalho fique com o turno real (Manha/Tarde)
        String turnoDoRelatorio = payload.frequencias().get(0).turno();

        int aulas6 = payload.totais().getOrDefault("6º Ano", 0);
        int aulas7 = payload.totais().getOrDefault("7º Ano", 0);
        int aulas8 = payload.totais().getOrDefault("8º Ano", 0);
        int aulas9 = payload.totais().getOrDefault("9º Ano", 0);

        var dadosRelatorio = new DadosRelatorioMensalDTO(
                mes,
                ano,
                "6º ao 9º Ano",
                turnoDoRelatorio, // enviando o turno dinamico pro gerador de pdf
                payload.assinaturaBase64(),
                "Esporte e Cidadania",
                "Coordenador do Projeto",
                aulas6,
                aulas7,
                aulas8,
                aulas9
        );

        byte[] arquivoPdf = pdfService.gerarRelatorioMensal(frequenciasSalvas, monitor, dadosRelatorio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_areninha.pdf\"")
                .body(arquivoPdf);
    }
}