package com.forkos.forkos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Usamos BigDecimal para cantidades decimales

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "producto_ingrediente") // Mapea a la tabla 'producto_ingrediente'
@Getter // Genera getters
@Setter // Genera setters
@NoArgsConstructor // Genera constructor sin argumentos (necesario para JPA)
@AllArgsConstructor // Genera constructor con todos los argumentos
public class ProductoIngrediente {

    @Id // Clave primaria para esta entidad intermedia
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autogenerado por la base de datos
    private Long id; // Aunque la DB use clave compuesta, podemos usar un ID simple aquí por comodidad en JPA

    @ManyToOne(fetch = FetchType.LAZY) // Muchos ProductoIngrediente pertenecen a un solo Producto
    @JoinColumn(name = "producto_id", nullable = false) // Define la columna de clave foránea hacia 'productos', no puede ser NULL
    private Producto producto; // Relación con la entidad Producto

    @ManyToOne(fetch = FetchType.LAZY) // Muchos ProductoIngrediente pertenecen a un solo Ingrediente
    @JoinColumn(name = "ingrediente_id", nullable = false) // Define la columna de clave foránea hacia 'ingredientes', no puede ser NULL
    private Ingrediente ingrediente; // Relación con la entidad Ingrediente

    @Column(name = "cantidad_necesaria", nullable = false) // Mapea a 'cantidad_necesaria', no puede ser NULL
    private BigDecimal cantidadNecesaria; // Corresponde a la columna 'cantidad_necesaria'. Usamos BigDecimal para precisión decimal.

    // Lombok se encarga de getters, setters, constructores
}
