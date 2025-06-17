package com.forkos.forkos.controller;

import com.forkos.forkos.dto.request.ProductoRequestDTO;
import com.forkos.forkos.model.Producto; // Importa la entidad Producto
import com.forkos.forkos.service.ProductoService; // Importa la interfaz del servicio
import com.forkos.forkos.dto.ProductoResponseDTO; // Importa el DTO para la respuesta de productos
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired
import org.springframework.http.HttpStatus; // Para códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Para envolver respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones comunes de web

import java.util.List;
import java.util.Optional;

@RestController // Indica a Spring que esta clase es un controlador REST
@RequestMapping("/api/productos") // Define la ruta base para todos los endpoints en este controlador (ej: /api/productos)
public class ProductoController {

    private final ProductoService productoService; // Variable para el servicio de productos

    // Inyección de dependencia del servicio
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Endpoint para obtener todos los productos
    // GET http://localhost:8080/api/productos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        List<ProductoResponseDTO> productos = productoService.getAllProductos(); // Llama al servicio
        return ResponseEntity.ok(productos); // Retorna la lista de productos con estado HTTP 200 OK
    }

    // Endpoint para obtener un producto por su ID
    // GET http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    @GetMapping("/{id}") // {id} es una variable de ruta
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) { // @PathVariable mapea el {id} de la URL al parámetro 'id' del método
        Optional<ProductoResponseDTO> producto = productoService.getProductoById(id); // Llama al servicio
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get()); // Si se encontró el producto, retornarlo con estado 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Si no se encontró, retornar estado 404 Not Found
        }
    }

    // Endpoint para crear un nuevo producto
    // POST http://localhost:8080/api/productos
    // El cuerpo de la petición debe contener los datos del producto en formato JSON
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody ProductoRequestDTO productoRequest) {
        try {
            ProductoResponseDTO nuevoProducto = productoService.createProducto(productoRequest);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            // Podrías devolver un mensaje de error más específico si quieres
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint para actualizar un producto existente
    // PUT http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    // El cuerpo de la petición debe contener los datos actualizados del producto en formato JSON
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequestDTO productoRequest) {
        try {
            ProductoResponseDTO productoActualizado = productoService.updateProducto(id, productoRequest);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar un producto
    // DELETE http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        // Opcional: Podrías querer verificar si el producto existe antes de intentar eliminar
        Optional<ProductoResponseDTO> existingProduct = productoService.getProductoById(id);
        if (!existingProduct.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si el producto no existe
        }

        productoService.deleteProducto(id); // Llama al servicio para eliminar
        // Retorna una respuesta vacía con estado HTTP 204 No Content (indicando éxito sin contenido en la respuesta)
        return ResponseEntity.noContent().build();
    }

    // Este método manejará peticiones como: GET http://localhost:8080/api/productos/categoria/1
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosPorCategoria(@PathVariable Long categoriaId) {
        List<ProductoResponseDTO> productos = productoService.getProductosByCategoriaId(categoriaId);
        return ResponseEntity.ok(productos); // Devuelve la lista de productos filtrados
    }

    // Puedes añadir otros endpoints relacionados con productos aquí (ej: buscar por nombre, por categoría)
}