package com.victor.escola_areninha.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.victor.escola_areninha.dto.DadosRelatorioMensalDTO;
import com.victor.escola_areninha.model.Frequencia;
import com.victor.escola_areninha.model.Usuario;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class PdfService {

    public byte[] gerarRelatorioMensal(List<Frequencia> frequencias, Usuario monitor, DadosRelatorioMensalDTO config) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTituloRelatorio = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10); // fonte menorzinha pra ficar elegante
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

            PdfPTable tableLogos = new PdfPTable(3);
            tableLogos.setWidthPercentage(50);

            tableLogos.addCell(criarCelulaLogo("images/SME.png"));
            tableLogos.addCell(criarCelulaLogo("images/Areninha_logo.png"));
            tableLogos.addCell(criarCelulaLogo("images/UECE.png"));

            document.add(tableLogos);
            document.add(Chunk.NEWLINE);

            Paragraph p1 = new Paragraph("PROJETO ESCOLA ARENINHAS: ESPORTE E EDUCAÇÃO EM TEMPO INTEGRAL", fontCabecalho);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            Paragraph p2 = new Paragraph("CONVÊNIO 2025", fontCabecalho);
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);
            document.add(Chunk.NEWLINE);

            String areninhaNome = frequencias.isEmpty() ? "Não definida" : frequencias.get(0).getAreninha().getNome();

            String nomeMes = Month.of(config.mes()).getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).toUpperCase();
            String mesAnoFormatado = nomeMes + "/" + (config.ano() % 100);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{2.5f, 1.5f});

            PdfPCell cellArea = new PdfPCell(new Paragraph("ÁREA DE CONHECIMENTO: " + config.areaConhecimento().toUpperCase(), fontBold));
            cellArea.setColspan(2);
            cellArea.setPadding(5);
            infoTable.addCell(cellArea);

            PdfPCell cellAreninha = new PdfPCell(new Paragraph("NOME DA ARENINHA: " + areninhaNome, fontNormal));
            cellAreninha.setPadding(5);
            infoTable.addCell(cellAreninha);

            PdfPCell cellTurmas = new PdfPCell(new Paragraph("TURMAS: " + config.turmas(), fontNormal));
            cellTurmas.setPadding(5);
            infoTable.addCell(cellTurmas);

            PdfPCell cellMonitor = new PdfPCell(new Paragraph("NOME DO/A MONITOR/A: " + monitor.getNome(), fontNormal));
            cellMonitor.setColspan(2);
            cellMonitor.setPadding(5);
            infoTable.addCell(cellMonitor);

            PdfPCell cellMes = new PdfPCell(new Paragraph("MÊS: " + mesAnoFormatado, fontNormal));
            cellMes.setPadding(5);
            infoTable.addCell(cellMes);

            PdfPCell cellTurno = new PdfPCell(new Paragraph("TURNOS: " + config.turno(), fontNormal));
            cellTurno.setPadding(5);
            infoTable.addCell(cellTurno);

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            Paragraph tituloRelatorio = new Paragraph("RELATÓRIO DE ATIVIDADES", fontTituloRelatorio);
            tituloRelatorio.setAlignment(Element.ALIGN_CENTER);
            document.add(tituloRelatorio);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2.5f, 5f, 2.5f});

            String[] headers = {"DIA", "HORA", "ATIVIDADE", "ASSINATURA"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, fontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(5);
                cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
                table.addCell(cell);
            }

            Image imgAssinatura = null;
            if (config.assinaturaUrl() != null && !config.assinaturaUrl().isEmpty()) {
                try {
                    imgAssinatura = Image.getInstance(new URL(config.assinaturaUrl()));
                    imgAssinatura.scaleToFit(60, 25);
                } catch (Exception e) {
                    System.out.println("Erro ao carregar imagem da assinatura.");
                }
            }

            DateTimeFormatter formatterDia = DateTimeFormatter.ofPattern("dd/MM");

            for (Frequencia freq : frequencias) {
                PdfPCell cellDia = new PdfPCell(new Paragraph(freq.getData().format(formatterDia), fontNormal));
                cellDia.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellDia.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellDia);

                PdfPCell cellHora = new PdfPCell(new Paragraph(freq.getHorario(), fontNormal));
                cellHora.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellHora.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cellHora);

                PdfPCell cellAtiv = new PdfPCell(new Paragraph(freq.getAtividade(), fontNormal));
                cellAtiv.setPadding(5);
                table.addCell(cellAtiv);

                PdfPCell cellAssinatura = new PdfPCell();
                if (imgAssinatura != null) {
                    cellAssinatura.addElement(imgAssinatura);
                }
                cellAssinatura.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellAssinatura.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellAssinatura.setMinimumHeight(35f);
                table.addCell(cellAssinatura);
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            String turnoLow = config.turno().toLowerCase();
            String textoRodape = String.format(
                    "O monitor %s ministrou %d aulas no 6º ano %s, %d aulas no 7º ano %s, %d aulas no 8º ano %s e %d aulas no 9º ano %s, totalizando uma carga horária mensal de 80 horas.\n(Considerando número de semanas e ou feriados ocorridos em seus dias de aulas).",
                    monitor.getNome(),
                    config.aulas6ano(), turnoLow,
                    config.aulas7ano(), turnoLow,
                    config.aulas8ano(), turnoLow,
                    config.aulas9ano(), turnoLow
            );

            Paragraph pRodape = new Paragraph(textoRodape, fontNormal);
            pRodape.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(pRodape);

            document.add(Chunk.NEWLINE);

            DateTimeFormatter fmtRodape = DateTimeFormatter.ofPattern("'Fortaleza,' dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
            Paragraph pData = new Paragraph(java.time.LocalDate.now().format(fmtRodape), fontNormal);
            // metendo o alinhamento no centro de tudo
            pData.setAlignment(Element.ALIGN_CENTER);
            document.add(pData);

            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph pAssinaturaResp = new Paragraph("____________________________________________\n" + config.nomeCoordenador() + "\nCoordenador(a) de Área", fontBold);
            pAssinaturaResp.setAlignment(Element.ALIGN_CENTER);
            document.add(pAssinaturaResp);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao montar o PDF do Relatório", e);
        }
    }

    private PdfPCell criarCelulaLogo(String imagePath) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        try {
            // usando a ferramenta raiz do spring pra achar o arquivo
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] bytes = is.readAllBytes();
                    Image img = Image.getInstance(bytes);
                    img.scaleToFit(60, 60);
                    img.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(img);
                }
            } else {
                System.out.println("Logo nao encontrada na pasta resources: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Erro pesadão ao carregar a logo: " + imagePath);
        }
        return cell;
    }
}