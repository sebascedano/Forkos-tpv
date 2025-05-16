package com.forkos.forkos.service.impl;

import com.forkos.forkos.model.Producto; // Importa la entidad
import com.forkos.forkos.repository.ProductoRepository; // Importa el repositorio
import com.forkos.forkos.service.ProductoService; // Importa la interfaz del servicio

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica a Spring que esta clase es un componente de servicio
public class ProductoServiceImpl implements ProductoService { // Implementa la interfaz ProductoService

    private final ProductoRepository productoRepository; // Variable para el repositorio

    // Inyección de dependencia del repositorio
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Implementación de los métodos definidos en ProductoService
    @Override
    public List<Producto> getAllProductos() {
        // La lógica de negocio (aquí simple) llama al repositorio
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProductoById(Long id) {
        // Lógica de negocio...
        return productoRepository.findById(id);
    }

    @Override
    public Producto saveProducto(Producto producto) {
        // Lógica de negocio (validaciones, etc. - aquí solo guarda)
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProductoById(Long id) {
        // Lógica de negocio...
        productoRepository.deleteById(id);
    }

    // Puedes añadir implementaciones para otros métodos de ProductoService aquí
}