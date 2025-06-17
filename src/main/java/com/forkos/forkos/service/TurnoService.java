package com.forkos.forkos.service;

import com.forkos.forkos.model.Turno;

import java.util.Optional;

public interface TurnoService {
    Turno iniciarNuevoTurno();
    Turno cerrarTurnoActual();
    Optional<Turno> getTurnoActual();
}
