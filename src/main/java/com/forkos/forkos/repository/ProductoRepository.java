package com.forkos.forkos.repository;

import com.forkos.forkos.model.Producto; // Importa la entidad Producto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importa List si defines métodos que retornan múltiples resultados
import java.util.Optional; // Importa Optional si defines métodos que puedan no encontrar resultados

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Spring Data JPA creará automáticamente una consulta para contar las filas
     * donde el campo 'stock' sea menor o igual que el valor proporcionado.
     *
     * @param stockLevel El umbral del stock.
     * @return El número de productos que cumplen la condición.
     */
    long countByStockLessThanEqual(Integer stockLevel);

    /**
     * Encuentra todos los productos que pertenecen a una categoría específica.
     *
     * @param categoriaId El ID de la categoría.
     * @return Una lista de productos que pertenecen a la categoría especificada.
     */
    List<Producto> findByCategoriaId(Long categoriaId);
}