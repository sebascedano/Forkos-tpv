// En el paquete service.impl de tu backend
package com.forkos.forkos.service.impl;

import com.forkos.forkos.dto.CategoriaDTO;
import com.forkos.forkos.dto.ProductoResponseDTO;
import com.forkos.forkos.dto.request.ProductoRequestDTO;
import com.forkos.forkos.model.Categoria;
import com.forkos.forkos.model.Producto;
import com.forkos.forkos.repository.CategoriaRepository;
import com.forkos.forkos.repository.ProductoRepository;
import com.forkos.forkos.service.ProductoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<ProductoResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream().map(this::mapProductoToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductoResponseDTO> getProductoById(Long id) {
        return productoRepository.findById(id).map(this::mapProductoToDTO);
    }

    @Override
    public List<ProductoResponseDTO> getProductosByCategoriaId(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::mapProductoToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoResponseDTO createProducto(ProductoRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);
        return mapProductoToDTO(guardado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));

        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);
        return mapProductoToDTO(actualizado);
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Método de mapeo completo y correcto
    private ProductoResponseDTO mapProductoToDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        BeanUtils.copyProperties(producto, dto, "categoria"); // Copia todo excepto la categoría

        if (producto.getCategoria() != null) {
            CategoriaDTO catDto = new CategoriaDTO();
            BeanUtils.copyProperties(producto.getCategoria(), catDto);
            dto.setCategoria(catDto); // Asigna el objeto CategoriaDTO
        }
        return dto;
    }


}