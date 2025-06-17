package com.forkos.forkos.service;
import com.forkos.forkos.dto.CategoriaDTO; // Asume que tienes este DTO
import java.util.List;

public interface CategoriaService {
    List<CategoriaDTO> getAllCategorias();
    List<CategoriaDTO> getCategoriasByNombre(String nombre);
    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);
    void eliminarCategoria(Long id);
}
