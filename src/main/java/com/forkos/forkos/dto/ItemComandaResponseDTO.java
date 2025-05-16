package com.forkos.forkos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Para precio unitario

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemComandaResponseDTO {
    private Long id;
    private Integer cantidad;
    private String notas;
    private BigDecimal precioUnitario;
    private String estado;

    // Incluir información del producto asociado (solo lo relevante para la respuesta del ítem)
    private Long productoId;
    private String productoNombre;
    // Puedes añadir más campos del producto si los necesitas en la respuesta del ítem
}
