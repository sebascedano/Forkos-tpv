package com.forkos.forkos.controller;

import com.forkos.forkos.dto.CategoriaDTO;
import com.forkos.forkos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias") // La URL base para este controlador
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService; // Inyecta el servicio

    // Este método manejará las peticiones GET a http://localhost:8080/api/categorias
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        // Llama al servicio para obtener los datos
        List<CategoriaDTO> categorias = categoriaService.getAllCategorias();
        // Devuelve la lista con un estado 200 OK
        return ResponseEntity.ok(categorias);
    }

    // --- ENDPOINT NUEVO PARA CREAR ---
    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    // --- ENDPOINT NUEVO PARA ELIMINAR ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // 404 si no se encuentra
        }
    }
}