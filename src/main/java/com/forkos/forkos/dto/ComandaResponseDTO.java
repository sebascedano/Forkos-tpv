package com.forkos.forkos.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime; // Para fecha y hora
import java.math.BigDecimal; // Para total
import java.util.List; // Para la lista de ítems
import java.util.ArrayList; // Para inicializar la lista

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComandaResponseDTO {
    private Long id;
    private LocalDateTime fechaHoraApertura;
    private LocalDateTime fechaHoraCierre;
    private String estado;
    private BigDecimal total;

    // Incluir información básica de la Mesa asociada
    private Long mesaId;
    private String mesaNumero; // Asegúrate de que tu entidad Mesa tenga un campo 'numero' y su getter

    // Incluir información básica del Mozo (Usuario) asociado
    private Long mozoId;
    private String mozoNombre; // Asegúrate de que tu entidad Usuario tenga un campo 'nombre' y su getter

    // Lista de ítems de comanda, representados por sus DTOs (NO entidades ItemComanda)
    private List<ItemComandaResponseDTO> items = new ArrayList<>(); // Inicializar para evitar NullPointerException

}