package com.forkos.forkos.repository;

import com.forkos.forkos.model.Ingrediente; // Importa la entidad Ingrediente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // Optional<Ingrediente> findByNombre(String nombre); // Encontrar ingrediente por nombre
    // List<Ingrediente> findByStockActualLessThan(Integer stockActual); // Encontrar ingredientes con stock bajo
}