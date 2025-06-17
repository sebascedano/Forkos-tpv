package com.forkos.forkos.service.impl;

import com.forkos.forkos.model.EstadoTurno;
import com.forkos.forkos.model.Turno;
import com.forkos.forkos.repository.ComandaRepository;
import com.forkos.forkos.repository.TurnoRepository;
import com.forkos.forkos.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired private ComandaRepository comandaRepository; // Para validación

    @Override
    public Turno iniciarNuevoTurno() {
        // Validación: No se puede iniciar un turno si ya hay uno abierto.
        if (turnoRepository.findByEstado(EstadoTurno.ABIERTO).isPresent()) {
            throw new IllegalStateException("Ya existe un turno abierto. Ciérrelo antes de iniciar uno nuevo.");
        }
        Turno nuevoTurno = new Turno();
        nuevoTurno.setFechaHoraApertura(LocalDateTime.now());
        nuevoTurno.setEstado(EstadoTurno.ABIERTO);
        return turnoRepository.save(nuevoTurno);
    }

    @Override
    public Turno cerrarTurnoActual() {
        Turno turnoAbierto = getTurnoActual().orElseThrow(() -> new IllegalStateException("No hay ningún turno abierto para cerrar."));

        // Validación CRÍTICA: No permitir cerrar turno si hay comandas abiertas.
        long comandasAbiertas = comandaRepository.countByEstado("ABIERTA");
        if (comandasAbiertas > 0) {
            throw new IllegalStateException("No se puede cerrar el turno. Existen " + comandasAbiertas + " comandas activas.");
        }

        turnoAbierto.setFechaHoraCierre(LocalDateTime.now());
        turnoAbierto.setEstado(EstadoTurno.CERRADO);
        return turnoRepository.save(turnoAbierto);
    }

    @Override
    public Optional<Turno> getTurnoActual() {
        return turnoRepository.findByEstado(EstadoTurno.ABIERTO);
    }
}
