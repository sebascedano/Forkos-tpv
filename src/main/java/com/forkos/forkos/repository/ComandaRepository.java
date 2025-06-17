package com.forkos.forkos.repository;

import com.forkos.forkos.model.Comanda; // Importa la entidad Comanda
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByEstado(String estado); // Encontrar comandas por estado

    // Nuevo método para encontrar una comanda por ID de mesa y estado específico
    Optional<Comanda> findByMesaIdAndEstado(Long mesaId, String estado);

    // Método para verificar si existe una comanda con un ID de mesa y estado específico
    boolean existsByMesaIdAndEstado(Long mesaId, String estado);

    // Método para encontrar todas las comandas con un estado específico y dentro de un rango de fechas
    List<Comanda> findByEstadoAndFechaHoraCierreBetween(String estado, LocalDateTime inicioDelDia, LocalDateTime finDelDia);

    @Query("SELECT COALESCE(SUM(c.cantidadComensales), 0) FROM Comanda c WHERE c.estado = ?1 AND c.fechaHoraCierre BETWEEN ?2 AND ?3")
    Integer sumCantidadComensalesByEstadoAndFechaHoraCierreBetween(String estado, LocalDateTime start, LocalDateTime end);
    // Método para contar el número de comandas con un estado específico
    long countByEstado(String estado);
}
