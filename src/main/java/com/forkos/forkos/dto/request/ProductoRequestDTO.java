package com.forkos.forkos.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data // Lombok genera getters, setters, toString, etc.
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId; // Solo necesitamos el ID de la categoría para asociarla
}
