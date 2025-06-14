package com.forkos.forkos.repository;

import com.forkos.forkos.model.Rol; // Importa la entidad Rol
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre); // Método para buscar un rol por su nombre
}
