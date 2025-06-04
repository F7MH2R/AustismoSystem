package com.autismo.neuroprevia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoSeguimiento(String destinatario, String nombrePaciente, String nombreDoctor, LocalDate fechaCita, String notas) {
        String asunto = "Nueva cita de seguimiento agendada";
        String cuerpo = String.format("""
                Estimado/a %s:

                Se ha agendado una nueva cita de seguimiento con el doctor %s.
                📅 Fecha de la cita: %s
                📝 Observaciones: %s

                Por favor, asegúrese de asistir puntualmente.

                Atentamente,
                Equipo de NeuroPrevia
                """, nombrePaciente, nombreDoctor, fechaCita.toString(), notas != null ? notas : "Sin observaciones");

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        mailSender.send(mensaje);
    }
}

