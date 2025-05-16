package com.forkos.forkos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredientes") // Mapea a la tabla 'ingredientes'
@Getter // Genera getters
@Setter // Genera setters
@NoArgsConstructor // Genera constructor sin argumentos (necesario para JPA)
@AllArgsConstructor

public class Ingrediente {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autogenerado por la base de datos
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100) // Mapea a 'nombre', es NOT NULL y UNIQUE
    private String nombre; // Corresponde a la columna 'nombre'

    @Column(name = "unidad", length = 20) // Mapea a 'unidad' (VARCHAR(20))
    private String unidad; // Corresponde a la columna 'unidad'

    @Column(name = "stock_actual") // Mapea a 'stock_actual' (INT)
    private Integer stockActual; // Corresponde a la columna 'stock_actual'. Usamos Integer para permitir NULL.

    @Column(name = "stock_minimo") // Mapea a 'stock_minimo' (INT)
    private Integer stockMinimo;
}
