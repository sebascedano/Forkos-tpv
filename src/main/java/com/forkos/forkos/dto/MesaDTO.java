package com.forkos.forkos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Usar Lombok para simplificar getters, setters, constructores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MesaDTO {
    private Long id;
    private String numero;
    private Integer capacidad;
    private String estado; // El estado se manejará como String en el DTO y en el backend
}