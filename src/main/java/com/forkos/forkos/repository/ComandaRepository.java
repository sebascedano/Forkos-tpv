package com.forkos.forkos.repository;

import com.forkos.forkos.model.Comanda; // Importa la entidad Comanda
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByEstado(String estado); // Encontrar comandas por estado

    // Nuevo método para encontrar una comanda por ID de mesa y estado específico
    Optional<Comanda> findByMesaIdAndEstado(Long mesaId, String estado);

    // Método para verificar si existe una comanda con un ID de mesa y estado específico
    boolean existsByMesaIdAndEstado(Long mesaId, String estado);
}
