package com.forkos.forkos.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime; // Usamos LocalDateTime para fechas y horas
import java.math.BigDecimal; // Usamos BigDecimal para el total
import java.util.List; // Para la relación OneToMany

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "comandas") // Mapea a la tabla 'comandas'
@Getter // Genera getters
@Setter // Genera setters
@NoArgsConstructor // Genera constructor sin argumentos (necesario para JPA)
@AllArgsConstructor // Genera constructor con todos los argumentos (útil, aunque quizás necesites constructores personalizados)
public class Comanda {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autogenerado por la base de datos
    private Long id; // Corresponde a la columna 'id'

    @ManyToOne(fetch = FetchType.LAZY) // Muchas Comandas pertenecen a una sola Mesa
    @JoinColumn(name = "mesa_id") // Define la columna de clave foránea hacia 'mesas'
    private Mesa mesa; // Relación con la entidad Mesa

    @ManyToOne(fetch = FetchType.LAZY) // Muchas Comandas fueron tomadas por un solo Usuario (mozo)
    @JoinColumn(name = "mozo_id") // Define la columna de clave foránea hacia 'usuarios'
    private Usuario mozo; // Relación con la entidad Usuario

    @Column(name = "fecha_hora_apertura", nullable = false) // Mapea a 'fecha_hora_apertura', no puede ser NULL
    private LocalDateTime fechaHoraApertura; // Usamos LocalDateTime para la fecha y hora

    @Column(name = "fecha_hora_cierre") // Mapea a 'fecha_hora_cierre'
    private LocalDateTime fechaHoraCierre; // Puede ser NULL

    @Column(name = "estado", nullable = false, length = 50) // Mapea a 'estado', no puede ser NULL
    private String estado; // Corresponde a la columna 'estado'

    @Column(name = "cantidad_comensales") // Optional: you can define a column name explicitly
    private Integer cantidadComensales;

    private BigDecimal total; // Mapea a 'total'. Usamos BigDecimal para precisión monetaria.



    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true) // Una Comanda tiene muchos ItemComanda
    // 'mappedBy' indica el nombre del campo en la entidad ItemComanda que posee la relación (@ManyToOne Comanda comanda;)
    // CascadeType.ALL: si guardas/borras una comanda, las operaciones se aplican a sus items también.
    // orphanRemoval = true: si quitas un item de la lista de items de una comanda, se borra de la base de datos.
    private List<ItemComanda> items; // Relación con la entidad ItemComanda

    // Lombok genera getters, setters, constructores
}