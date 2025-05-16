package com.forkos.forkos.repository;

import com.forkos.forkos.model.ProductoIngrediente; // Importa la entidad ProductoIngrediente
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa @Repository
// -----------------------------------------------

@Repository
public interface ProductoIngredienteRepository extends JpaRepository<ProductoIngrediente, Long> {

    // Spring Data JPA proporcionará automáticamente los métodos CRUD básicos (save, findById, findAll, delete, etc.)

    // Opcional: Puedes añadir métodos de búsqueda personalizados aquí si los necesitas
}