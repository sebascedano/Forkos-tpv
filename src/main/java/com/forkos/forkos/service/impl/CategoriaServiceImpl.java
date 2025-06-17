package com.forkos.forkos.service.impl;

import com.forkos.forkos.dto.CategoriaDTO;
import com.forkos.forkos.model.Categoria;
import com.forkos.forkos.repository.CategoriaRepository;
import com.forkos.forkos.service.CategoriaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaDTO> getAllCategorias() {
        // 1. Llama al repositorio para obtener todas las entidades Categoria
        List<Categoria> categorias = categoriaRepository.findAll();

        // 2. Usa un stream para convertir cada entidad Categoria a un CategoriaDTO
        return categorias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaDTO> getCategoriasByNombre(String nombre) {
        // 1. Llama al repositorio para buscar categorías por nombre
        List<Categoria> categorias = categoriaRepository.findByNombre(nombre);

        // 2. Convierte las entidades a DTOs
        return categorias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    // --- MÉTODO NUEVO PARA CREAR ---
    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        // Para una lista, la forma correcta de saber si tiene algo es con !.isEmpty()
        if (!categoriaRepository.findByNombre(categoriaDTO.getNombre()).isEmpty()) {
            throw new IllegalStateException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDTO.getNombre());
        nuevaCategoria.setDescripcion(categoriaDTO.getDescripcion());

        Categoria guardada = categoriaRepository.save(nuevaCategoria);
        return convertToDto(guardada);
    }

    // --- MÉTODO NUEVO PARA ELIMINAR ---
    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("No se encontró la categoría con ID: " + id);
        }
        // TODO: Añadir lógica para verificar que la categoría no esté en uso por algún producto antes de borrar
        categoriaRepository.deleteById(id);
    }

    // Método de ayuda para la conversión
    private CategoriaDTO convertToDto(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        // Copia las propiedades con nombres iguales (id, nombre, descripcion)
        BeanUtils.copyProperties(categoria, categoriaDTO);
        return categoriaDTO;
    }
}