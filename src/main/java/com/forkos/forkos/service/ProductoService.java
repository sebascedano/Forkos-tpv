// En el paquete service de tu backend
package com.forkos.forkos.service;

import com.forkos.forkos.dto.ProductoResponseDTO;
import com.forkos.forkos.dto.request.ProductoRequestDTO;
import com.forkos.forkos.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    // Todos los métodos ahora devuelven DTOs
    List<ProductoResponseDTO> getAllProductos();

    Optional<ProductoResponseDTO> getProductoById(Long id);

    List<ProductoResponseDTO> getProductosByCategoriaId(Long categoriaId);

    ProductoResponseDTO createProducto(ProductoRequestDTO productoRequest);

    ProductoResponseDTO updateProducto(Long id, ProductoRequestDTO productoRequest);

    void deleteProducto(Long id);

    Producto saveProducto(Producto producto);
}