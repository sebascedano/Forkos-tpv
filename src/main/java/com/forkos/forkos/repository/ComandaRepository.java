package com.forkos.forkos.repository;

import com.forkos.forkos.model.Comanda; // Importa la entidad Comanda
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // List<Comanda> findByMesaId(Long mesaId); // Encontrar comandas por ID de mesa
    // List<Comanda> findByMozoId(Long mozoId); // Encontrar comandas por ID de mozo
    List<Comanda> findByEstado(String estado); // Encontrar comandas por estado
    // Optional<Comanda> findByMesaIdAndEstado(Long mesaId, String estado); // Encontrar comanda abierta para una mesa
}