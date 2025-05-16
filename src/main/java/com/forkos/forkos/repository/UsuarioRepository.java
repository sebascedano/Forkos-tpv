package com.forkos.forkos.repository;

import com.forkos.forkos.model.Usuario; // Importa la entidad Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
}