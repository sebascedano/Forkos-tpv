package com.forkos.forkos.controller;

import com.forkos.forkos.model.Producto; // Importa la entidad Producto
import com.forkos.forkos.service.ProductoService; // Importa la interfaz del servicio

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
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos(); // Llama al servicio
        return ResponseEntity.ok(productos); // Retorna la lista de productos con estado HTTP 200 OK
    }

    // Endpoint para obtener un producto por su ID
    // GET http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    @GetMapping("/{id}") // {id} es una variable de ruta
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) { // @PathVariable mapea el {id} de la URL al parámetro 'id' del método
        Optional<Producto> producto = productoService.getProductoById(id); // Llama al servicio
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
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) { // @RequestBody mapea el JSON del cuerpo de la petición a un objeto Producto Java
        Producto savedProducto = productoService.saveProducto(producto); // Llama al servicio para guardar el producto
        // Retorna el producto guardado con estado HTTP 201 Created
        // Puedes usar HttpStatus.CREATED directamente o ResponseEntity.status(HttpStatus.CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    // Endpoint para actualizar un producto existente
    // PUT http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    // El cuerpo de la petición debe contener los datos actualizados del producto en formato JSON
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        // Opcional: Podrías querer verificar si el producto con el ID existe antes de actualizar
        Optional<Producto> existingProduct = productoService.getProductoById(id);
        if (!existingProduct.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si el producto no existe
        }

        // Aseguramos que el ID del producto a actualizar sea el de la URL
        producto.setId(id);
        Producto updatedProducto = productoService.saveProducto(producto); // Llama al servicio para guardar (actualizar)
        return ResponseEntity.ok(updatedProducto); // Retorna el producto actualizado con estado 200 OK
    }

    // Endpoint para eliminar un producto
    // DELETE http://localhost:8080/api/productos/{id} (ej: /api/productos/1)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        // Opcional: Podrías querer verificar si el producto existe antes de intentar eliminar
        Optional<Producto> existingProduct = productoService.getProductoById(id);
        if (!existingProduct.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si el producto no existe
        }

        productoService.deleteProductoById(id); // Llama al servicio para eliminar
        // Retorna una respuesta vacía con estado HTTP 204 No Content (indicando éxito sin contenido en la respuesta)
        return ResponseEntity.noContent().build();
    }

    // Puedes añadir otros endpoints relacionados con productos aquí (ej: buscar por nombre, por categoría)
}