package com.forkos.forkos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String passwordHash;
    private String pin;
    private boolean activo=true;

    @Column(nullable=false, unique=true)//username obligatorio y único
    private String username;

    @ManyToOne(fetch = FetchType.EAGER)//Se cargan los roles con el usuario para autorización
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
