package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.*;
import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.model.dto.RespuestaDetalleDto;
import com.autismo.neuroprevia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private examenRealizadoRepository examenRealizadoRepo;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private examenRepository examenRepository;

    @Autowired
    private seguimientoRepository seguimientoRepo;

    @Autowired
    private respuestaDadaRepository respuestaDadaRepo;

    @Autowired
    private preguntaRepository preguntaRepo;

    @Autowired
    private respuestaPosibleRepository respuestaPosibleRepo;


    public List<InformeDto> obtenerInformesRealizados() {
        List<ExamenRealizado> realizados = examenRealizadoRepo.findAll();

        return realizados.stream().map(er -> {
            InformeDto dto = new InformeDto();

            dto.setId((long) er.getId()); // si InformeDto espera Long
            dto.setFechaRealizacion(er.getFechaRealizacion().toLocalDateTime());
            dto.setResultadoTotal(calcularResultadoTotal((long) er.getId()));
            dto.setInterpretacion(er.getInterpretacion());

            Usuario paciente = er.getUsuario();
            dto.setNombrePaciente(paciente != null ? paciente.getNombre() + " " + paciente.getApellido() : "Desconocido");

            Examen examen = er.getExamen();
            dto.setExamenTitulo(examen != null ? examen.getTitulo() : "Sin título");

            return dto;
        }).toList();
    }

    public InformeDto obtenerInformePorId(Long id) {
        ExamenRealizado er = examenRealizadoRepo.findById(id.intValue()).orElseThrow();

        InformeDto dto = new InformeDto();
        dto.setId((long) er.getId());
        dto.setFechaRealizacion(er.getFechaRealizacion().toLocalDateTime());
        // ✅ Calcula el total sumando los valores de las respuestas
        double resultadoTotal = calcularResultadoTotal(id);
        dto.setResultadoTotal(resultadoTotal);
        dto.setInterpretacion(er.getInterpretacion());

        Usuario paciente = er.getUsuario();
        dto.setNombrePaciente(paciente != null ? paciente.getNombre() + " " + paciente.getApellido() : "Desconocido");

        Examen examen = er.getExamen();
        dto.setExamenTitulo(examen != null ? examen.getTitulo() : "Sin título");

        return dto;
    }

    public List<RespuestaDetalleDto> obtenerRespuestasPorInforme(Long idExamenRealizado) {
        List<RespuestaDada> dadas = respuestaDadaRepo.findByExamenRealizado_Id(idExamenRealizado.intValue());

        return dadas.stream().map(r -> {
            Pregunta p = r.getPregunta();
            RespuestaPosible rp = r.getRespuesta();
            return new RespuestaDetalleDto(p, rp);
        }).toList();
    }

    public double calcularResultadoTotal(Long idExamenRealizado) {
        List<RespuestaDada> respuestas = respuestaDadaRepo.findByExamenRealizado_Id(idExamenRealizado.intValue());

        return respuestas.stream()
                .mapToDouble(r -> r.getRespuesta().getValorNumerico())
                .sum();
    }




    public List<Seguimiento> obtenerSeguimientosDelDoctor(int idDoctor) {
        return seguimientoRepo.findByIdDoctor(idDoctor);
    }
}

