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
    public ResponseEntity<Frequencia> registrar(@RequestBody DadosCadastroFrequenciaDTO dados, Principal principal) {
        // Descobre quem é o monitor pelo Token
        var responsavel = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario logado nao encontrado no banco"));

        var frequencia = new Frequencia();
        frequencia.setData(dados.data());
        frequencia.setAtividade(dados.atividade());
        frequencia.setHorario(dados.horario());
        frequencia.setResponsavel(responsavel);
        frequencia.setPdfUrl(null);
        frequencia.setTurno(dados.turno());

        // Pega a areninha vinculada ao perfil do monitor!
        frequencia.setAreninha(responsavel.getAreninha());

        // Salva e devolve o objeto completo (com ID gerado) pro celular
        var frequenciaSalva = repository.save(frequencia);
        return ResponseEntity.ok(frequenciaSalva);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<List<Frequencia>> listarHistorico(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            Principal principal) {

        var responsavel = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        // se o front n mandar nada, usa a data de hoje
        int m = (mes != null) ? mes : LocalDate.now().getMonthValue();
        int a = (ano != null) ? ano : LocalDate.now().getYear();

        // pega do dia 1 ate o ultimo dia do mes
        LocalDate inicioMes = LocalDate.of(a, m, 1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        var historico = repository.findByResponsavelIdAndDataBetweenOrderByDataDesc(
                responsavel.getId(), inicioMes, fimMes);

        return ResponseEntity.ok(historico);
    }

    @PostMapping("/relatorio")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<byte[]> gerarRelatorioDaLista(@RequestBody RelatorioPayloadDTO payload, Principal principal) {

        var monitor = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Frequencia> frequenciasParaPdf = new ArrayList<>();

        for (var dto : payload.frequencias()) {
            var freq = new Frequencia();
            // Lemos a data direto no formato padrão (YYYY-MM-DD)
            freq.setData(LocalDate.parse(dto.data()));
            freq.setAtividade(dto.atividade());
            freq.setHorario(dto.horario());
            freq.setTurno(dto.turno());
            freq.setResponsavel(monitor);
            freq.setAreninha(monitor.getAreninha());

            // Apenas adicionamos à lista do PDF, NÃO salvamos no banco novamente!
            frequenciasParaPdf.add(freq);
        }

        int mes = frequenciasParaPdf.get(0).getData().getMonthValue();
        int ano = frequenciasParaPdf.get(0).getData().getYear();
        String turnoDoRelatorio = frequenciasParaPdf.get(0).getTurno();

        int aulas6 = payload.totais().getOrDefault("6º Ano", 0);
        int aulas7 = payload.totais().getOrDefault("7º Ano", 0);
        int aulas8 = payload.totais().getOrDefault("8º Ano", 0);
        int aulas9 = payload.totais().getOrDefault("9º Ano", 0);

        var dadosRelatorio = new DadosRelatorioMensalDTO(
                mes, ano, "6º ao 9º Ano", turnoDoRelatorio,
                payload.assinaturaBase64(), "Esporte e Cidadania",
                "Coordenador do Projeto", aulas6, aulas7, aulas8, aulas9
        );

        byte[] arquivoPdf = pdfService.gerarRelatorioMensal(frequenciasParaPdf, monitor, dadosRelatorio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_areninha.pdf\"")
                .body(arquivoPdf);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<Frequencia> editarFrequencia(@PathVariable Long id, @RequestBody DadosCadastroFrequenciaDTO dados) {
        var frequencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));

        frequencia.setData(dados.data()); // Pode ser LocalDate.parse(dados.data()) se reclamar de tipo
        frequencia.setAtividade(dados.atividade());
        frequencia.setHorario(dados.horario());
        frequencia.setTurno(dados.turno());

        return ResponseEntity.ok(repository.save(frequencia));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MONITOR', 'SUPERVISOR', 'ADMIN')")
    public ResponseEntity<Void> apagarFrequencia(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}