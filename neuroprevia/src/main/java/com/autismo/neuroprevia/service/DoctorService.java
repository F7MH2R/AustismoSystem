package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Seguimiento;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.seguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private examenRealizadoRepository examenRealizadoRepo;

    @Autowired
    private seguimientoRepository seguimientoRepo;

    // Punto 4 – Obtener informes clínicos
    public List<ExamenRealizado> obtenerInformesRealizados() {
        return examenRealizadoRepo.findAll();
    }

    // Punto 5 – Obtener seguimientos de un doctor específico
    public List<Seguimiento> obtenerSeguimientosDelDoctor(int idDoctor) {
        return seguimientoRepo.findByIdDoctor(idDoctor);
    }
}
