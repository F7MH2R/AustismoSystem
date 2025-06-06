package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.model.dto.RespuestaDetalleDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    public byte[] generarInformePDF(InformeDto informe, List<RespuestaDetalleDto> respuestas) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document documento = new Document(PageSize.A4);
            PdfWriter.getInstance(documento, baos);
            documento.open();

            // Título
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            documento.add(new Paragraph("Informe Clínico de " + informe.getNombrePaciente(), fontTitulo));
            documento.add(new Paragraph(" ")); // Espacio

            // Datos principales
            documento.add(new Paragraph("Examen: " + informe.getExamenTitulo()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String fechaFormateada = informe.getFechaRealizacion().format(formatter);
            documento.add(new Paragraph("Fecha de realización: " + fechaFormateada));

           // documento.add(new Paragraph("Fecha de realización: " + informe.getFechaRealizacion().toLocalDate()+" "+informe.getFechaRealizacion().toLocalTime()));
            documento.add(new Paragraph("Resultado total: " + informe.getResultadoTotal()));
            documento.add(new Paragraph("Interpretación: " + informe.getInterpretacion()));
            documento.add(new Paragraph(" "));

            // Tabla de respuestas
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.addCell("#");
            tabla.addCell("Pregunta");
            tabla.addCell("Respuesta");

            int contador = 1;
            for (RespuestaDetalleDto r : respuestas) {
                tabla.addCell(String.valueOf(contador++));
                tabla.addCell(r.getPregunta().getTexto());
                tabla.addCell(r.getRespuesta().getTextoRespuesta());
            }

            documento.add(tabla);
            documento.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
}
