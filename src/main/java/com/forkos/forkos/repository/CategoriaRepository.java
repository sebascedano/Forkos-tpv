package com.forkos.forkos.repository;

import com.forkos.forkos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Aquí puedes definir métodos personalizados si es necesario
    // Por ejemplo, para buscar categorías por nombre:
    List<Categoria> findByNombre(String nombre);
}
