package com.victor.escola_areninha.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.victor.escola_areninha.model.Frequencia;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] gerarPdfFrequencia(Frequencia frequencia) {
        // usa o array em memoria pra nao precisar salvar o arquivo fisico no disco do servidor
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);

            document.open();

            // metendo um titulo padrao
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Registro de Frequência - Decofin", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(Chunk.NEWLINE);

            // formata a data pro padrao br
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = frequencia.getData().format(formatter);

            // corpo do documento com os dados do banco
            document.add(new Paragraph("Areninha: " + frequencia.getAreninha().getNome()));
            document.add(new Paragraph("Data da Atividade: " + dataFormatada));
            document.add(new Paragraph("Horário: " + frequencia.getHorario()));
            document.add(new Paragraph("Atividade/Turma: " + frequencia.getAtividade()));
            document.add(new Paragraph("Monitor Responsável: " + frequencia.getResponsavel().getNome()));

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // espaco pra assinar dps q imprimir
            document.add(new Paragraph("___________________________________________________"));
            Paragraph assinatura = new Paragraph("Assinatura do Responsável");
            assinatura.setAlignment(Element.ALIGN_CENTER);
            document.add(assinatura);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            // estora a excecao se der boleta na geracao
            throw new RuntimeException("Erro interno ao montar o PDF", e);
        }
    }
}