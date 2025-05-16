package com.forkos.forkos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne; // Para las relaciones a Comanda y Producto
import jakarta.persistence.JoinColumn; // Para definir las columnas de clave foránea
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column; // Para notas y estado si no usan el nombre exacto del campo

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Usamos BigDecimal para precio unitario

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "item_comanda") // Mapea a la tabla 'item_comanda'
@Getter // Genera getters
@Setter // Genera setters
@NoArgsConstructor // Genera constructor sin argumentos (necesario para JPA)
@AllArgsConstructor // Genera constructor con todos los argumentos (útil)
public class ItemComanda {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autogenerado por la base de datos
    private Long id; // Corresponde a la columna 'id'

    @ManyToOne(fetch = FetchType.LAZY) // Muchos ItemComanda pertenecen a una sola Comanda
    @JoinColumn(name = "comanda_id", nullable = false) // Define la columna de clave foránea hacia 'comandas', no puede ser NULL
    private Comanda comanda; // Relación con la entidad Comanda

    @ManyToOne(fetch = FetchType.LAZY) // Muchos ItemComanda se refieren a un solo Producto
    @JoinColumn(name = "producto_id") // Define la columna de clave foránea hacia 'productos'
    private Producto producto; // Relación con la entidad Producto

    @Column(nullable = false) // Mapea a 'cantidad', no puede ser NULL
    private Integer cantidad; // Corresponde a la columna 'cantidad' (INT)

    @Column(name = "precio_unitario", nullable = false) // Mapea a 'precio_unitario', no puede ser NULL
    private BigDecimal precioUnitario; // Corresponde a la columna 'precio_unitario'. Usamos BigDecimal.

    private String notas; // Mapea a 'notas' (TEXT) - Por defecto es nullable

    @Column(name = "estado", length = 50) // Mapea a 'estado' (VARCHAR(50))
    private String estado; // Estado de este item particular (ej: "PENDIENTE", "EN_COCINA", "SERVido")

    // Lombok genera getters, setters, constructores
}
