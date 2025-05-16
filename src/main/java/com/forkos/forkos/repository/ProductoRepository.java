package com.forkos.forkos.repository;

import com.forkos.forkos.model.Producto; // Importa la entidad Producto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importa List si defines métodos que retornan múltiples resultados
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // Optional<Producto> findByNombre(String nombre); // Encontrar producto por nombre
    // List<Producto> findByCategoriaId(Long categoriaId); // Encontrar productos por categoría
    // List<Producto> findByActivoTrue(); // Encontrar solo productos activos
}