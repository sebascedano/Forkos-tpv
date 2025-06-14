package com.forkos.forkos.repository;

import com.forkos.forkos.model.Usuario; // Importa la entidad Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Metodos de busqueda personalizados:
    //Búsqueda por username
    Optional<Usuario> findByUsername(String username);
    //Búsqueda por email
    Optional<Usuario> findByEmail(String email);

}