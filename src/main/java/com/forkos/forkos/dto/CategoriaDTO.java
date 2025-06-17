package com.forkos.forkos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;

}
