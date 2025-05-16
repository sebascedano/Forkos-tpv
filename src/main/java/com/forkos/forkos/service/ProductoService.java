package com.forkos.forkos.service;

import com.forkos.forkos.model.Producto; // Importa la entidad Producto
import java.util.List;
import java.util.Optional;

// Interfaz que define el contrato del servicio de Productos
public interface ProductoService {

    List<Producto> getAllProductos(); // Obtener todos los productos
    Optional<Producto> getProductoById(Long id); // Obtener un producto por su ID
    Producto saveProducto(Producto producto); // Crear o actualizar un producto
    void deleteProductoById(Long id); // Eliminar un producto por su ID

    // Aquí definirás todos los métodos de negocio relacionados con Productos
}