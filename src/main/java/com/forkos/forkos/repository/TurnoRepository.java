package com.forkos.forkos.repository;

import com.forkos.forkos.model.EstadoTurno;
import com.forkos.forkos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Método para encontrar un Turno por su estado
    Optional<Turno> findByEstado(EstadoTurno estado);
}
