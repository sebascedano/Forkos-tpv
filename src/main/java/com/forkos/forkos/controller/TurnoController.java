package com.forkos.forkos.controller;

import com.forkos.forkos.model.Turno;
import com.forkos.forkos.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarTurno() {
        try {
            return ResponseEntity.ok(turnoService.iniciarNuevoTurno());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/cerrar")
    public ResponseEntity<?> cerrarTurno() {
        try {
            return ResponseEntity.ok(turnoService.cerrarTurnoActual());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/actual")
    public ResponseEntity<Turno> getTurnoActual() {
        return ResponseEntity.of(turnoService.getTurnoActual());
    }
}
