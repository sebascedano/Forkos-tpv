package com.forkos.forkos.repository;

import com.forkos.forkos.model.Mesa; // Importa la entidad Mesa
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importa List si defines métodos que retornan múltiples resultados
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // Optional<Mesa> findByNumero(String numero); // Encontrar mesa por número (ya pusimos UNIQUE en el modelo)
    // List<Mesa> findByEstado(String estado); // Encontrar mesas por estado
}
