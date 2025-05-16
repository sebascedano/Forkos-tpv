package com.forkos.forkos.repository;

import com.forkos.forkos.model.Rol; // Importa la entidad Rol
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // Optional<Rol> findByNombre(String nombre); // Encontrar rol por nombre (pusimos UNIQUE en el modelo)
}
