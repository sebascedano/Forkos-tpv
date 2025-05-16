package com.forkos.forkos.repository;

import com.forkos.forkos.model.ItemComanda; // Importa la entidad ItemComanda
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemComandaRepository extends JpaRepository<ItemComanda, Long> {

    // Spring Data JPA proporciona automáticamente métodos CRUD básicos.

    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // List<ItemComanda> findByComandaId(Long comandaId); // Encontrar todos los items de una comanda
    // List<ItemComanda> findByProductoId(Long productoId); // Encontrar todos los items que usan un producto
    // List<ItemComanda> findByEstado(String estado); // Encontrar items por estado (ej: "EN_COCINA")
}